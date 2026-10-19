package com.triplestar.learn.data.repository

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.triplestar.learn.R
import com.triplestar.learn.data.model.Task
import kotlinx.coroutines.tasks.await

class TaskRepository(private val db: FirebaseFirestore) {
    private val auth = Firebase.auth

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in.")
    }

    suspend fun saveTask(task: Task) {
        val uid = requireUserId()
        val taskWithUid = task.copy(userId = uid)
        db.collection("users").document(uid).collection("tasks").add(taskWithUid).await()
    }
}
