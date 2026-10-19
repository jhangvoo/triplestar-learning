package com.triplestar.learn.data.model

import com.google.firebase.Timestamp

enum class LearningLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

data class ChildProfile(
    val id: String = "",
    val parentId: String = "",
    val displayName: String = "",
    val avatarId: String = "avatar_star",
    val age: Int = 7,
    val learningLevel: String = LearningLevel.BEGINNER.name,
    val dailyTargetMinutes: Int = 20,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
