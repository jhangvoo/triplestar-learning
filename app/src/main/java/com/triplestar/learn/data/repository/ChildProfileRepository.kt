package com.triplestar.learn.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.triplestar.learn.R
import com.triplestar.learn.data.model.ChildProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class ChildProfileRepository(private val context: Context) {

    private fun getFirestore(): FirebaseFirestore? {
        val app = try {
            FirebaseApp.getInstance()
        } catch (e: Exception) {
            return null
        }
        val dbId = try {
            val resId = context.resources.getIdentifier("firestore_database_id", "string", context.packageName)
            if (resId != 0) context.getString(resId) else ""
        } catch (e: Exception) {
            ""
        }
        return try {
            if (dbId.isNotBlank() && dbId != "(default)") {
                FirebaseFirestore.getInstance(app, dbId)
            } else {
                FirebaseFirestore.getInstance(app)
            }
        } catch (e: Exception) {
            try {
                FirebaseFirestore.getInstance(app)
            } catch (ex: Exception) {
                null
            }
        }
    }

    fun observeChildren(parentId: String): Flow<List<ChildProfile>> {
        if (parentId.isBlank()) return flowOf(emptyList())
        val authUid = try {
            com.google.firebase.Firebase.auth.currentUser?.uid ?: System.getProperty("TEST_AUTH_UID")
        } catch (e: Exception) {
            System.getProperty("TEST_AUTH_UID")
        }
        if (authUid == null || authUid != parentId) {
            // Unauthenticated or mismatched session; do not trigger unauthorized Firestore queries
            return flowOf(emptyList())
        }
        val db = getFirestore() ?: return flowOf(emptyList())

        return try {
            db.collection("parents")
                .document(parentId)
                .collection("children")
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(ChildProfile::class.java)
                }
                .catch { error ->
                    if (error is kotlinx.coroutines.CancellationException) {
                        Log.d("ChildProfileRepo", "Snapshot observation cancelled")
                        throw error
                    }
                    Log.e("ChildProfileRepo", "Error observing child profiles: ${error.message}", error)
                    emit(emptyList())
                }
        } catch (e: Exception) {
            Log.e("ChildProfileRepo", "Failed to setup snapshot listener", e)
            flowOf(emptyList())
        }
    }

    suspend fun createChildProfile(profile: ChildProfile): Result<Unit> {
        if (profile.parentId.isBlank()) {
            return Result.failure(IllegalArgumentException("Parent ID cannot be blank"))
        }
        val db = getFirestore() ?: return Result.failure(IllegalStateException("Firestore backend is not configured"))

        return try {
            val childDoc = if (profile.id.isNotBlank()) {
                db.collection("parents").document(profile.parentId).collection("children").document(profile.id)
            } else {
                db.collection("parents").document(profile.parentId).collection("children").document()
            }

            val toSave = profile.copy(
                id = childDoc.id,
                createdAt = profile.createdAt ?: Timestamp.now(),
                updatedAt = Timestamp.now()
            )
            childDoc.set(toSave).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ChildProfileRepo", "Error creating child profile", e)
            Result.failure(e)
        }
    }

    suspend fun deleteChildProfile(parentId: String, childId: String): Result<Unit> {
        val db = getFirestore() ?: return Result.failure(IllegalStateException("Firestore backend is not configured"))
        return try {
            db.collection("parents")
                .document(parentId)
                .collection("children")
                .document(childId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ChildProfileRepo", "Error deleting child profile", e)
            Result.failure(e)
        }
    }
}
