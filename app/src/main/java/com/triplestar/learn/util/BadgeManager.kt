package com.triplestar.learn.util

import com.triplestar.learn.data.model.Badge
import com.triplestar.learn.data.model.BadgeCategory
import com.triplestar.learn.data.model.BadgeRarity
import com.triplestar.learn.data.model.BadgeUnlockStatus
import com.triplestar.learn.data.model.ChildProgress

object BadgeManager {

    private val allBadges = listOf(
        // Math Module Badges
        Badge(
            id = "math_explorer",
            subjectId = "math",
            title = "Math Explorer",
            titleEn = "Math Explorer",
            titleUr = "میتھس ایکسپلورر",
            descriptionEn = "Awarded for completing your first Mathematics challenge!",
            descriptionUr = "ریاضی کا پہلا چیلنج مکمل کرنے پر اعزاز!",
            milestoneCriteriaEn = "Complete 1 Math Step",
            milestoneCriteriaUr = "ریاضی کا ۱ مرحلہ مکمل کریں",
            stickerEmoji = "🔢",
            iconName = "calculator",
            category = BadgeCategory.MATH,
            rarity = BadgeRarity.COMMON,
            colorHex = 0xFF42A5F5
        ),
        Badge(
            id = "math_whiz",
            subjectId = "math",
            title = "Math Whiz",
            titleEn = "Math Whiz",
            titleUr = "حساب کے استاد",
            descriptionEn = "Mastered multiple math arithmetic problems!",
            descriptionUr = "حساب کے کئی اہم سوالات کامیابی سے حل کیے!",
            milestoneCriteriaEn = "Complete 2 Math Steps",
            milestoneCriteriaUr = "ریاضی کے ۲ مراحل مکمل کریں",
            stickerEmoji = "🧮",
            iconName = "calculator",
            category = BadgeCategory.MATH,
            rarity = BadgeRarity.RARE,
            colorHex = 0xFF26A69A
        ),
        Badge(
            id = "math_ninja",
            subjectId = "math",
            title = "Number Ninja",
            titleEn = "Number Ninja",
            titleUr = "نمبر ننجا",
            descriptionEn = "Completed 3 Mathematics challenges with sharp precision!",
            descriptionUr = "مہارت اور تیزی سے ۳ ریاضی کے اسباق مکمل کیے!",
            milestoneCriteriaEn = "Complete 3 Math Steps",
            milestoneCriteriaUr = "ریاضی کے ۳ مراحل مکمل کریں",
            stickerEmoji = "📐",
            iconName = "calculator",
            category = BadgeCategory.MATH,
            rarity = BadgeRarity.EPIC,
            colorHex = 0xFFAB47BC
        ),
        Badge(
            id = "math_star_champ",
            subjectId = "math",
            title = "Math Star Champion",
            titleEn = "Math Star Champion",
            titleUr = "ریاضی کے ستارے",
            descriptionEn = "Earned 3 or more Stars in Mathematics!",
            descriptionUr = "ریاضی میں ۳ یا اس سے زیادہ ستارے حاصل کیے!",
            milestoneCriteriaEn = "Earn 3 Math Stars",
            milestoneCriteriaUr = "ریاضی میں ۳ ستارے کمائیں",
            stickerEmoji = "🌟",
            iconName = "star",
            category = BadgeCategory.MATH,
            rarity = BadgeRarity.LEGENDARY,
            colorHex = 0xFFFFB300
        ),

        // English Module Badges
        Badge(
            id = "english_phonics",
            subjectId = "english",
            title = "Phonics Pioneer",
            titleEn = "Phonics Pioneer",
            titleUr = "حروف کے کھوجی",
            descriptionEn = "Took your first step in English sounds and reading!",
            descriptionUr = "انگریزی پڑھنے اور صوتیات میں پہلا قدم اٹھایا!",
            milestoneCriteriaEn = "Complete 1 English Step",
            milestoneCriteriaUr = "انگریزی کا ۱ مرحلہ مکمل کریں",
            stickerEmoji = "🔤",
            iconName = "alphabet",
            category = BadgeCategory.ENGLISH,
            rarity = BadgeRarity.COMMON,
            colorHex = 0xFFFF7043
        ),
        Badge(
            id = "english_wordsmith",
            subjectId = "english",
            title = "Word Wizard",
            titleEn = "Word Wizard",
            titleUr = "الفاظ کے جادوگر",
            descriptionEn = "Expanded vocabulary and sentence comprehension!",
            descriptionUr = "الفاظ کا ذخیرہ اور جملوں کی سمجھ بوجھ بڑھائی!",
            milestoneCriteriaEn = "Complete 2 English Steps",
            milestoneCriteriaUr = "انگریزی کے ۲ مراحل مکمل کریں",
            stickerEmoji = "📖",
            iconName = "book_open",
            category = BadgeCategory.ENGLISH,
            rarity = BadgeRarity.RARE,
            colorHex = 0xFF5C6BC0
        ),
        Badge(
            id = "english_storyteller",
            subjectId = "english",
            title = "Storyteller Star",
            titleEn = "Storyteller Star",
            titleUr = "کہانی ساز چیمپئن",
            descriptionEn = "Completed 3 English literacy reading & speaking challenges!",
            descriptionUr = "انگریزی میں ۳ اسباق مکمل کر کے کہانیوں میں مہارت حاصل کی!",
            milestoneCriteriaEn = "Complete 3 English Steps",
            milestoneCriteriaUr = "انگریزی کے ۳ مراحل مکمل کریں",
            stickerEmoji = "🗣️",
            iconName = "record_voice_over",
            category = BadgeCategory.ENGLISH,
            rarity = BadgeRarity.EPIC,
            colorHex = 0xFFEC407A
        ),
        Badge(
            id = "english_champion",
            subjectId = "english",
            title = "English Champion",
            titleEn = "English Champion",
            titleUr = "انگریزی چیمپئن",
            descriptionEn = "Earned 3 or more Stars in English Literacy!",
            descriptionUr = "انگریزی میں ۳ یا زیادہ ستارے حاصل کیے!",
            milestoneCriteriaEn = "Earn 3 English Stars",
            milestoneCriteriaUr = "انگریزی میں ۳ ستارے کمائیں",
            stickerEmoji = "🏆",
            iconName = "trophy",
            category = BadgeCategory.ENGLISH,
            rarity = BadgeRarity.LEGENDARY,
            colorHex = 0xFFFFCA28
        ),

        // General / Milestone Badges
        Badge(
            id = "dual_scholar",
            subjectId = "general",
            title = "Dual Genius",
            titleEn = "Dual Genius",
            titleUr = "دوہرا عبقری",
            descriptionEn = "Achieved milestones in BOTH Mathematics and English Literacy!",
            descriptionUr = "ریاضی اور انگریزی دونوں مضامین میں سنگ میل عبور کیے!",
            milestoneCriteriaEn = "Complete steps in Math & English",
            milestoneCriteriaUr = "ریاضی اور انگریزی دونوں میں سرگرمیاں مکمل کریں",
            stickerEmoji = "🚀",
            iconName = "rocket",
            category = BadgeCategory.GENERAL,
            rarity = BadgeRarity.EPIC,
            colorHex = 0xFF29B6F6
        ),
        Badge(
            id = "star_collector",
            subjectId = "general",
            title = "Star Collector",
            titleEn = "Star Collector",
            titleUr = "ستاروں کا سلطان",
            descriptionEn = "Collected 5 or more Stars across your learning adventures!",
            descriptionUr = "تمام مضامین میں مجموعی طور پر ۵ یا زیادہ ستارے کمائے!",
            milestoneCriteriaEn = "Earn 5 Total Stars",
            milestoneCriteriaUr = "مجموعی طور پر ۵ ستارے کمائیں",
            stickerEmoji = "⭐",
            iconName = "star",
            category = BadgeCategory.GENERAL,
            rarity = BadgeRarity.LEGENDARY,
            colorHex = 0xFFFFD54F
        )
    )

    fun getAllBadges(): List<Badge> = allBadges

    fun evaluateBadges(
        mathProgress: ChildProgress?,
        englishProgress: ChildProgress?
    ): List<BadgeUnlockStatus> {
        val mathSteps = mathProgress?.completedStepIds?.size ?: 0
        val mathStars = mathProgress?.totalStars ?: 0

        val englishSteps = englishProgress?.completedStepIds?.size ?: 0
        val englishStars = englishProgress?.totalStars ?: 0

        val totalStars = mathStars + englishStars

        return allBadges.map { badge ->
            val (isUnlocked, current, target) = when (badge.id) {
                "math_explorer" -> Triple(mathSteps >= 1, minOf(mathSteps, 1), 1)
                "math_whiz" -> Triple(mathSteps >= 2, minOf(mathSteps, 2), 2)
                "math_ninja" -> Triple(mathSteps >= 3, minOf(mathSteps, 3), 3)
                "math_star_champ" -> Triple(mathStars >= 3, minOf(mathStars, 3), 3)

                "english_phonics" -> Triple(englishSteps >= 1, minOf(englishSteps, 1), 1)
                "english_wordsmith" -> Triple(englishSteps >= 2, minOf(englishSteps, 2), 2)
                "english_storyteller" -> Triple(englishSteps >= 3, minOf(englishSteps, 3), 3)
                "english_champion" -> Triple(englishStars >= 3, minOf(englishStars, 3), 3)

                "dual_scholar" -> {
                    val count = (if (mathSteps >= 1) 1 else 0) + (if (englishSteps >= 1) 1 else 0)
                    Triple(count >= 2, count, 2)
                }
                "star_collector" -> Triple(totalStars >= 5, minOf(totalStars, 5), 5)

                else -> Triple(false, 0, 1)
            }

            BadgeUnlockStatus(
                badge = badge.copy(isEarned = isUnlocked),
                isUnlocked = isUnlocked,
                currentProgress = current,
                targetProgress = target
            )
        }
    }

    /**
     * Checks if completing a step in a subject unlocks a new badge.
     */
    fun checkForNewUnlock(
        subjectId: String,
        previousCompletedCount: Int,
        newCompletedCount: Int,
        previousStars: Int,
        newStars: Int
    ): Badge? {
        if (subjectId == "math") {
            if (previousCompletedCount < 1 && newCompletedCount >= 1) return allBadges.first { it.id == "math_explorer" }
            if (previousCompletedCount < 2 && newCompletedCount >= 2) return allBadges.first { it.id == "math_whiz" }
            if (previousCompletedCount < 3 && newCompletedCount >= 3) return allBadges.first { it.id == "math_ninja" }
            if (previousStars < 3 && newStars >= 3) return allBadges.first { it.id == "math_star_champ" }
        } else if (subjectId == "english") {
            if (previousCompletedCount < 1 && newCompletedCount >= 1) return allBadges.first { it.id == "english_phonics" }
            if (previousCompletedCount < 2 && newCompletedCount >= 2) return allBadges.first { it.id == "english_wordsmith" }
            if (previousCompletedCount < 3 && newCompletedCount >= 3) return allBadges.first { it.id == "english_storyteller" }
            if (previousStars < 3 && newStars >= 3) return allBadges.first { it.id == "english_champion" }
        }
        return null
    }
}
