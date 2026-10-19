package com.triplestar.learn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateBack: (() -> Unit)? = null,
    onAuthSuccess: ((userId: String, email: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isStatusError by remember { mutableStateOf(false) }

    // Firebase Auth Connection State
    var isFirebaseConnected by remember {
        mutableStateOf(FirebaseApp.getApps(context).isNotEmpty())
    }
    var currentUserEmail by remember {
        mutableStateOf(try { FirebaseAuth.getInstance().currentUser?.email } catch (_: Exception) { null })
    }

    // Update connection status helper
    fun checkFirebaseConnection(): String {
        return try {
            val apps = FirebaseApp.getApps(context)
            if (apps.isEmpty()) {
                isFirebaseConnected = false
                "Firebase is not initialized (no active apps configured)."
            } else {
                val app = FirebaseApp.getInstance()
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser
                isFirebaseConnected = true
                currentUserEmail = user?.email
                val projectId = app.options.projectId ?: "configured"
                if (user != null) {
                    "Connected to Firebase ($projectId). Signed in as: ${user.email}"
                } else {
                    "Connected to Firebase ($projectId). Ready for authentication."
                }
            }
        } catch (e: Exception) {
            isFirebaseConnected = false
            "Firebase connection error: ${e.localizedMessage ?: e.message}"
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Firebase Auth & Login", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("btn_back")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val status = checkFirebaseConnection()
                            statusMessage = status
                            isStatusError = !isFirebaseConnected
                            scope.launch { snackbarHostState.showSnackbar(status) }
                        },
                        modifier = Modifier.testTag("btn_test_connection_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Refresh Connection Status"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .testTag("login_screen_container"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Header Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AstroBluePrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LockPerson,
                    contentDescription = null,
                    tint = AstroBluePrimary,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Account Authentication",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "Authenticate with email & password and test Firebase Auth status.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Connection Status Banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isFirebaseConnected) {
                        EmeraldSuccess.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                    }
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_connection_status")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isFirebaseConnected) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = if (isFirebaseConnected) EmeraldSuccess else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isFirebaseConnected) "Firebase Auth: Connected" else "Firebase Auth: Disconnected",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isFirebaseConnected) EmeraldSuccess else MaterialTheme.colorScheme.error
                        )
                        if (currentUserEmail != null) {
                            Text(
                                text = "Logged in: $currentUserEmail",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            val msg = checkFirebaseConnection()
                            statusMessage = msg
                            isStatusError = !isFirebaseConnected
                        },
                        modifier = Modifier.testTag("btn_test_connection")
                    ) {
                        Text("Test", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                placeholder = { Text("user@example.com") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_email")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Input Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_password")
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    val cleanEmail = email.trim()
                    val cleanPassword = password.trim()
                    if (cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
                        statusMessage = "Please enter both email and password."
                        isStatusError = true
                        return@Button
                    }
                    isLoading = true
                    statusMessage = null
                    scope.launch {
                        try {
                            val auth = FirebaseAuth.getInstance()
                            val authResult = auth.signInWithEmailAndPassword(cleanEmail, cleanPassword).await()
                            val user = authResult.user
                            isLoading = false
                            if (user != null) {
                                currentUserEmail = user.email
                                isFirebaseConnected = true
                                statusMessage = "Authentication successful! User ID: ${user.uid}"
                                isStatusError = false
                                snackbarHostState.showSnackbar("Sign in successful! ⭐")
                                onAuthSuccess?.invoke(user.uid, user.email ?: cleanEmail)
                            } else {
                                statusMessage = "Sign in returned null user."
                                isStatusError = true
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            isStatusError = true
                            statusMessage = "Sign in failed: ${e.localizedMessage ?: e.message}"
                            snackbarHostState.showSnackbar(statusMessage ?: "Error")
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_login")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(imageVector = Icons.Default.Login, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign In", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Create Account / Register Button
            OutlinedButton(
                onClick = {
                    val cleanEmail = email.trim()
                    val cleanPassword = password.trim()
                    if (cleanEmail.isEmpty() || cleanPassword.length < 6) {
                        statusMessage = "Email must be valid and password at least 6 characters."
                        isStatusError = true
                        return@OutlinedButton
                    }
                    isLoading = true
                    statusMessage = null
                    scope.launch {
                        try {
                            val auth = FirebaseAuth.getInstance()
                            val authResult = auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword).await()
                            val user = authResult.user
                            isLoading = false
                            if (user != null) {
                                currentUserEmail = user.email
                                isFirebaseConnected = true
                                statusMessage = "Account created and connected! User ID: ${user.uid}"
                                isStatusError = false
                                snackbarHostState.showSnackbar("Account created successfully! 🎉")
                                onAuthSuccess?.invoke(user.uid, user.email ?: cleanEmail)
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            isStatusError = true
                            statusMessage = "Account creation failed: ${e.localizedMessage ?: e.message}"
                            snackbarHostState.showSnackbar(statusMessage ?: "Error")
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_register")
            ) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Account", style = MaterialTheme.typography.titleMedium)
            }

            // Sign Out Option if signed in
            if (currentUserEmail != null) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = {
                        try {
                            FirebaseAuth.getInstance().signOut()
                            currentUserEmail = null
                            statusMessage = "Signed out successfully."
                            isStatusError = false
                            scope.launch { snackbarHostState.showSnackbar("Signed out") }
                        } catch (e: Exception) {
                            statusMessage = "Sign out error: ${e.localizedMessage}"
                        }
                    },
                    modifier = Modifier.testTag("btn_sign_out")
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sign Out ($currentUserEmail)", color = MaterialTheme.colorScheme.error)
                }
            }

            // Inline Status Message
            if (statusMessage != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isStatusError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_status_result")
                ) {
                    Text(
                        text = statusMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isStatusError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
