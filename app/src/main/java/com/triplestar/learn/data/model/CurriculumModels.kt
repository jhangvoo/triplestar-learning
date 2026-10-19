package com.triplestar.learn.data.model

data class Subject(
    val id: String = "",
    val titleEn: String = "",
    val titleUr: String = "",
    val iconName: String = "book",
    val displayOrder: Int = 0,
    val isPublished: Boolean = true
)

data class Lesson(
    val id: String = "",
    val subjectId: String = "",
    val titleEn: String = "",
    val titleUr: String = "",
    val order: Int = 1,
    val minAge: Int = 5,
    val isPublished: Boolean = true
)

data class Question(
    val id: String = "",
    val questionEn: String = "",
    val questionUr: String = "",
    val optionsEn: List<String> = emptyList(),
    val optionsUr: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val explanationEn: String = "",
    val explanationUr: String = ""
)

data class Step(
    val id: String = "",
    val lessonId: String = "",
    val stepNumber: Int = 1,
    val stepType: String = StepType.LEARN.name,
    val titleEn: String = "",
    val titleUr: String = "",
    val contentEn: String = "",
    val contentUr: String = "",
    val questions: List<Question> = emptyList(),
    val xpReward: Int = 15,
    val starsReward: Int = 1
)
