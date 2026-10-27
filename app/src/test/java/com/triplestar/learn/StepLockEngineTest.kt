package com.triplestar.learn

import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepStatus
import com.triplestar.learn.data.model.StepType
import com.triplestar.learn.util.StepLockEngine
import org.junit.Assert.assertEquals
import org.junit.Test

class StepLockEngineTest {

    private val lessonSteps = listOf(
        Step(id = "step_1", lessonId = "lesson_1", stepNumber = 1, stepType = StepType.LEARN.name, xpReward = 10, starsReward = 1),
        Step(id = "step_2", lessonId = "lesson_1", stepNumber = 2, stepType = StepType.PRACTICE.name, xpReward = 15, starsReward = 1),
        Step(id = "step_3", lessonId = "lesson_1", stepNumber = 3, stepType = StepType.QUIZ.name, xpReward = 20, starsReward = 1)
    )

    @Test
    fun `step 1 is available for new learner with null progress`() {
        val status = StepLockEngine.resolveStepStatus(
            step = lessonSteps[0],
            allStepsInLesson = lessonSteps,
            progress = null
        )
        assertEquals(StepStatus.AVAILABLE, status)
    }

    @Test
    fun `step 2 is locked when step 1 is not completed`() {
        val status = StepLockEngine.resolveStepStatus(
            step = lessonSteps[1],
            allStepsInLesson = lessonSteps,
            progress = null
        )
        assertEquals(StepStatus.LOCKED, status)
    }

    @Test
    fun `step 2 becomes available when step 1 is completed`() {
        val progress = ChildProgress(
            id = "child1_math",
            childId = "child1",
            parentId = "parent1",
            subjectId = "math",
            completedStepIds = listOf("step_1"),
            totalXp = 10,
            totalStars = 1
        )
        val status = StepLockEngine.resolveStepStatus(
            step = lessonSteps[1],
            allStepsInLesson = lessonSteps,
            progress = progress
        )
        assertEquals(StepStatus.AVAILABLE, status)
    }

    @Test
    fun `completed step reflects completed status`() {
        val progress = ChildProgress(
            id = "child1_math",
            childId = "child1",
            parentId = "parent1",
            subjectId = "math",
            completedStepIds = listOf("step_1"),
            totalXp = 10,
            totalStars = 1
        )
        val status = StepLockEngine.resolveStepStatus(
            step = lessonSteps[0],
            allStepsInLesson = lessonSteps,
            progress = progress
        )
        assertEquals(StepStatus.COMPLETED, status)
    }

    @Test
    fun `computing progress after completion increments XP stars and unlocks next step`() {
        val initialProgress = ChildProgress(
            id = "child1_math",
            childId = "child1",
            parentId = "parent1",
            subjectId = "math",
            completedStepIds = emptyList(),
            unlockedStepIds = listOf("step_1"),
            totalXp = 0,
            totalStars = 0
        )

        val updatedProgress = StepLockEngine.computeProgressAfterStepCompletion(
            currentProgress = initialProgress,
            completedStep = lessonSteps[0],
            allStepsInLesson = lessonSteps
        )

        assertEquals(listOf("step_1"), updatedProgress.completedStepIds)
        assertEquals(listOf("step_1", "step_2"), updatedProgress.unlockedStepIds)
        assertEquals(10, updatedProgress.totalXp)
        assertEquals(1, updatedProgress.totalStars)
    }

    @Test
    fun `re-completing already completed step does not duplicate XP or stars`() {
        val completedProgress = ChildProgress(
            id = "child1_math",
            childId = "child1",
            parentId = "parent1",
            subjectId = "math",
            completedStepIds = listOf("step_1"),
            unlockedStepIds = listOf("step_1", "step_2"),
            totalXp = 10,
            totalStars = 1
        )

        val updatedProgress = StepLockEngine.computeProgressAfterStepCompletion(
            currentProgress = completedProgress,
            completedStep = lessonSteps[0],
            allStepsInLesson = lessonSteps
        )

        assertEquals(listOf("step_1"), updatedProgress.completedStepIds)
        assertEquals(10, updatedProgress.totalXp)
        assertEquals(1, updatedProgress.totalStars)
    }

    @Test
    fun `completed steps remain accessible for revision without resetting progress`() {
        val revisionProgress = ChildProgress(
            id = "child1_science",
            childId = "child1",
            parentId = "parent1",
            subjectId = "science",
            completedStepIds = listOf("step_1", "step_2"),
            unlockedStepIds = listOf("step_1", "step_2", "step_3"),
            totalXp = 25,
            totalStars = 2
        )

        val status1 = StepLockEngine.resolveStepStatus(
            step = lessonSteps[0],
            allStepsInLesson = lessonSteps,
            progress = revisionProgress
        )
        val status2 = StepLockEngine.resolveStepStatus(
            step = lessonSteps[1],
            allStepsInLesson = lessonSteps,
            progress = revisionProgress
        )

        assertEquals(StepStatus.COMPLETED, status1)
        assertEquals(StepStatus.COMPLETED, status2)
    }
}

