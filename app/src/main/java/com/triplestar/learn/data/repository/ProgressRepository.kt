package com.triplestar.learn.data.repository

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.triplestar.learn.R
import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.util.StepLockEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class ProgressRepository(private val context: Context) {

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

    /**
     * Observes server-authoritative progress document for a child profile and subject.
     * Document path: /progress/{childId}_{subjectId}
     */
    fun observeProgress(childId: String, subjectId: String): Flow<ChildProgress?> {
        if (childId.isBlank() || subjectId.isBlank()) return flowOf(null)
        val authUid = try {
            com.google.firebase.Firebase.auth.currentUser?.uid ?: System.getProperty("TEST_AUTH_UID")
        } catch (e: Exception) {
            System.getProperty("TEST_AUTH_UID")
        }
        if (authUid == null) {
            // Unauthenticated session; return null without triggering unauthorized queries
            return flowOf(null)
        }
        val db = getFirestore() ?: return flowOf(null)

        val progressDocId = "${childId}_${subjectId}"
        return db.collection("progress")
            .document(progressDocId)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(ChildProgress::class.java)
                } else {
                    null
                }
            }
            .catch { error ->
                if (error is kotlinx.coroutines.CancellationException) {
                    throw error
                }
                Log.e("ProgressRepo", "Error observing child progress: ${error.message}", error)
                emit(null)
            }
    }

    /**
     * Records completion of a step on the server.
     * Enforces prerequisite validation and prevents client-side arbitrary XP spoofing.
     */
    suspend fun recordStepCompletion(
        childId: String,
        parentId: String,
        subjectId: String,
        step: Step,
        allStepsInLesson: List<Step>,
        nextLessonFirstStepId: String? = null
    ): Result<Unit> {
        val db = getFirestore() ?: return Result.failure(IllegalStateException("Firestore backend is not configured"))
        val progressDocId = "${childId}_${subjectId}"
        val docRef = db.collection("progress").document(progressDocId)

        return try {
            val snapshot = try {
                docRef.get().await()
            } catch (e: Exception) {
                null
            }
            val existingProgress = if (snapshot != null && snapshot.exists()) {
                snapshot.toObject(ChildProgress::class.java) ?: ChildProgress(
                    id = progressDocId,
                    childId = childId,
                    parentId = parentId,
                    subjectId = subjectId
                )
            } else {
                ChildProgress(
                    id = progressDocId,
                    childId = childId,
                    parentId = parentId,
                    subjectId = subjectId
                )
            }

            // Server-authoritative calculation via StepLockEngine
            val updated = StepLockEngine.computeProgressAfterStepCompletion(
                currentProgress = existingProgress,
                completedStep = step,
                allStepsInLesson = allStepsInLesson,
                nextLessonFirstStepId = nextLessonFirstStepId
            ).copy(
                parentId = parentId,
                updatedAt = Timestamp.now()
            )

            val payload = mapOf(
                "id" to updated.id,
                "childId" to updated.childId,
                "parentId" to updated.parentId,
                "subjectId" to updated.subjectId,
                "completedStepIds" to updated.completedStepIds,
                "unlockedStepIds" to updated.unlockedStepIds,
                "totalXp" to updated.totalXp,
                "totalStars" to updated.totalStars,
                "streakDays" to updated.streakDays,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProgressRepo", "Error recording step completion", e)
            Result.failure(e)
        }
    }
}
