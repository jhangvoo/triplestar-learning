package com.triplestar.learn.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.triplestar.learn.R
import com.triplestar.learn.data.state.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val credentialManager = CredentialManager.create(context)

    init {
        checkInitialAuthState()
    }

    fun isBackendConfigured(): Boolean {
        return try {
            val appList = FirebaseApp.getApps(context)
            if (appList.isEmpty()) return false
            val resId = context.resources.getIdentifier("web_client_id", "string", context.packageName).let {
                if (it != 0) it else context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
            }
            resId != 0
        } catch (e: Exception) {
            false
        }
    }

    private fun checkInitialAuthState() {
        if (!isBackendConfigured()) {
            _authState.value = AuthState.BackendConfigPending(
                context.getString(R.string.auth_firebase_pending_desc)
            )
            return
        }

        try {
            val auth = Firebase.auth
            auth.addAuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user != null) {
                    _authState.value = AuthState.Authenticated(
                        userId = user.uid,
                        email = user.email ?: "",
                        displayName = user.displayName ?: context.getString(R.string.auth_parent_section)
                    )
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Failed to initialize Firebase Auth", e)
            _authState.value = AuthState.BackendConfigPending(
                context.getString(R.string.auth_firebase_pending_desc)
            )
        }
    }

    fun attemptSilentAutoSignIn(scope: CoroutineScope) {
        if (!isBackendConfigured()) return

        val clientIdResId = context.resources.getIdentifier("web_client_id", "string", context.packageName)
        if (clientIdResId == 0) return
        val serverClientId = context.getString(clientIdResId)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                    Firebase.auth.signInWithCredential(authCredential).await()
                }
            } catch (e: Exception) {
                Log.d("AuthRepository", "Silent auto-sign-in skipped: ${e.message}")
            }
        }
    }

    fun signInWithGoogle(
        activity: Activity,
        scope: CoroutineScope,
        onSuccess: (() -> Unit)? = null,
        onError: (String) -> Unit
    ) {
        Log.d("AuthRepository", "signInWithGoogle invoked. Activity: ${activity.javaClass.name}, Scope: $scope")

        if (!isBackendConfigured()) {
            val pendingDesc = context.getString(R.string.auth_firebase_pending_desc)
            Log.w("AuthRepository", "Backend is not configured: $pendingDesc")
            onError(pendingDesc)
            return
        }

        val clientIdResId = context.resources.getIdentifier("web_client_id", "string", context.packageName).let {
            if (it != 0) it else context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        }
        if (clientIdResId == 0) {
            val errorMsg = "Neither 'web_client_id' nor 'default_web_client_id' string resource found in app"
            Log.e("AuthRepository", errorMsg)
            onError(errorMsg)
            return
        }
        val serverClientId = context.getString(clientIdResId)
        Log.d("AuthRepository", "Using serverClientId: $serverClientId for GetSignInWithGoogleOption")

        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = serverClientId).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInOption)
            .build()

        _authState.value = AuthState.Loading
        Log.d("AuthRepository", "Set AuthState to Loading. Launching coroutine for CredentialManager.getCredential...")

        scope.launch {
            try {
                Log.d("AuthRepository", "Executing credentialManager.getCredential(activity, request)...")
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential
                Log.d("AuthRepository", "credentialManager.getCredential succeeded. Class=${credential.javaClass.name}, type=${credential.type}")

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    Log.d("AuthRepository", "Extracted Google ID token (length=${idToken.length}). Calling Firebase signInWithCredential...")
                    val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                    val authResult = Firebase.auth.signInWithCredential(authCredential).await()
                    val user = authResult.user
                    if (user != null) {
                        Log.i("AuthRepository", "Firebase authentication successful! UID=${user.uid}, email=${user.email}, name=${user.displayName}")
                        _authState.value = AuthState.Authenticated(
                            userId = user.uid,
                            email = user.email ?: "",
                            displayName = user.displayName ?: context.getString(R.string.auth_parent_section)
                        )
                        onSuccess?.invoke()
                    } else {
                        Log.e("AuthRepository", "Firebase sign-in completed but FirebaseUser is null")
                        _authState.value = AuthState.Unauthenticated
                        onError("Authentication succeeded but user profile was null")
                    }
                } else {
                    val msg = "Unexpected credential type returned: ${credential.type}"
                    Log.w("AuthRepository", msg)
                    _authState.value = AuthState.Unauthenticated
                    onError(msg)
                }
            } catch (e: GetCredentialCancellationException) {
                Log.i("AuthRepository", "Google Sign-In cancelled by user: ${e.message}")
                _authState.value = AuthState.Unauthenticated
            } catch (e: GetCredentialException) {
                Log.e("AuthRepository", "CredentialManager GetCredentialException: type=${e.type}, message=${e.message}", e)
                _authState.value = AuthState.Unauthenticated
                onError(e.message ?: "Google Sign-In failed (${e.type})")
            } catch (e: Exception) {
                Log.e("AuthRepository", "Unexpected exception during Google Sign-In: ${e.javaClass.name}: ${e.message}", e)
                _authState.value = AuthState.Unauthenticated
                onError(e.localizedMessage ?: "Sign in failed")
            }
        }
    }

    fun signOut(scope: CoroutineScope, onComplete: () -> Unit) {
        try {
            if (isBackendConfigured()) {
                Firebase.auth.signOut()
            }
        } catch (e: Exception) {
            Log.w("AuthRepository", "Error during Firebase sign-out", e)
        }
        scope.launch {
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } catch (e: Exception) {
                Log.w("AuthRepository", "Error clearing credentials", e)
            } finally {
                _authState.value = if (isBackendConfigured()) AuthState.Unauthenticated else {
                    AuthState.BackendConfigPending(context.getString(R.string.auth_firebase_pending_desc))
                }
                onComplete()
            }
        }
    }
}
