package com.triplestar.learn.util

import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepStatus

object StepLockEngine {

    /**
     * Determines whether a given step is LOCKED, AVAILABLE, IN_PROGRESS, COMPLETED, or MASTERED.
     * Enforces prerequisite rules:
     * 1. Completed steps remain COMPLETED or MASTERED.
     * 2. Steps explicitly in unlockedStepIds are AVAILABLE.
     * 3. Prerequisite lessons must be completed before advancing to the next lesson.
     * 4. Step N cannot be accessed until Step N-1 is verified as completed.
     */
    fun resolveStepStatus(
        step: Step,
        allStepsInLesson: List<Step>,
        progress: ChildProgress?,
        isPrerequisiteLessonCompleted: Boolean = true
    ): StepStatus {
        if (progress == null) {
            // First step in the first lesson is available for new learners only if prerequisite is satisfied
            return if (step.stepNumber == 1 && isPrerequisiteLessonCompleted) StepStatus.AVAILABLE else StepStatus.LOCKED
        }

        val completedSet = progress.completedStepIds.toSet()
        val unlockedSet = progress.unlockedStepIds.toSet()

        if (completedSet.contains(step.id)) {
            // Check for full mastery condition (e.g., quiz completed with maximum stars)
            val isMastered = step.stepNumber == allStepsInLesson.maxOfOrNull { it.stepNumber } && progress.totalStars >= 3
            return if (isMastered) StepStatus.MASTERED else StepStatus.COMPLETED
        }

        // If step is explicitly in unlocked set from the backend or prior progression
        if (unlockedSet.contains(step.id)) {
            return StepStatus.AVAILABLE
        }

        // Prerequisite lesson must be completed before any non-completed step in this lesson is unlocked
        if (!isPrerequisiteLessonCompleted) {
            return StepStatus.LOCKED
        }

        // If prerequisite lesson is completed, step 1 of this lesson is available
        if (step.stepNumber == 1) {
            return StepStatus.AVAILABLE
        }

        // Check prerequisite step within this lesson
        val previousStep = allStepsInLesson.firstOrNull { it.stepNumber == step.stepNumber - 1 }
        return if (previousStep != null && completedSet.contains(previousStep.id)) {
            StepStatus.AVAILABLE
        } else {
            StepStatus.LOCKED
        }
    }

    /**
     * Server-authoritative step unlock validator.
     * Generates updated progress payload with next step unlocked only if current step is valid.
     * Prevents duplicate XP and stars when a completed step is submitted repeatedly.
     * Unlocks next lesson's first step when the last step of the current lesson is completed.
     */
    fun computeProgressAfterStepCompletion(
        currentProgress: ChildProgress,
        completedStep: Step,
        allStepsInLesson: List<Step>,
        nextLessonFirstStepId: String? = null
    ): ChildProgress {
        val alreadyCompleted = currentProgress.completedStepIds.contains(completedStep.id)
        val updatedCompleted = (currentProgress.completedStepIds + completedStep.id).distinct()
        val nextStepInLesson = allStepsInLesson.firstOrNull { it.stepNumber == completedStep.stepNumber + 1 }

        val newUnlockedStepId = when {
            nextStepInLesson != null -> nextStepInLesson.id
            nextLessonFirstStepId != null -> nextLessonFirstStepId
            else -> null
        }

        val updatedUnlocked = if (newUnlockedStepId != null) {
            (currentProgress.unlockedStepIds + newUnlockedStepId).distinct()
        } else {
            currentProgress.unlockedStepIds
        }

        return currentProgress.copy(
            completedStepIds = updatedCompleted,
            unlockedStepIds = updatedUnlocked,
            totalXp = if (alreadyCompleted) currentProgress.totalXp else currentProgress.totalXp + completedStep.xpReward,
            totalStars = if (alreadyCompleted) currentProgress.totalStars else currentProgress.totalStars + completedStep.starsReward
        )
    }
}
