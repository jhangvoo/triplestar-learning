package com.triplestar.learn.data.model

import com.google.firebase.Timestamp

data class ChildProgress(
    val id: String = "", // compound: "${childId}_${subjectId}"
    val childId: String = "",
    val parentId: String = "",
    val subjectId: String = "",
    val completedStepIds: List<String> = emptyList(),
    val unlockedStepIds: List<String> = emptyList(),
    val totalXp: Int = 0,
    val totalStars: Int = 0,
    val streakDays: Int = 0,
    val lastActiveDate: String = "",
    val updatedAt: Timestamp? = null
)
