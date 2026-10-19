package com.triplestar.learn.data.model

enum class BadgeCategory {
    MATH,
    ENGLISH,
    GENERAL
}

enum class BadgeRarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY
}

data class Badge(
    val id: String = "",
    val subjectId: String = "",
    val title: String = "",
    val titleEn: String = title,
    val titleUr: String = "",
    val descriptionEn: String = "",
    val descriptionUr: String = "",
    val milestoneCriteriaEn: String = "",
    val milestoneCriteriaUr: String = "",
    val stickerEmoji: String = "⭐",
    val iconName: String = "trophy",
    val category: BadgeCategory = BadgeCategory.GENERAL,
    val rarity: BadgeRarity = BadgeRarity.COMMON,
    val isEarned: Boolean = false,
    val colorHex: Long = 0xFFFFD700
)

data class BadgeUnlockStatus(
    val badge: Badge,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val targetProgress: Int
)
