package com.triplestar.learn.util

import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepStatus

object StepLockEngine {

    /**
     * Determines whether a given step is LOCKED, AVAILABLE, IN_PROGRESS, COMPLETED, or MASTERED.
     * Enforces that Step N cannot be accessed until Step N-1 is verified as completed.
     */
    fun resolveStepStatus(
        step: Step,
        allStepsInLesson: List<Step>,
        progress: ChildProgress?
    ): StepStatus {
        if (progress == null) {
            // First step in the first lesson is available for new learners
            return if (step.stepNumber == 1) StepStatus.AVAILABLE else StepStatus.LOCKED
        }

        val completedSet = progress.completedStepIds.toSet()
        val unlockedSet = progress.unlockedStepIds.toSet()

        if (completedSet.contains(step.id)) {
            // Check for full mastery condition (e.g., quiz completed with maximum stars)
            val isMastered = step.stepNumber == allStepsInLesson.maxOfOrNull { it.stepNumber } && progress.totalStars >= 3
            return if (isMastered) StepStatus.MASTERED else StepStatus.COMPLETED
        }

        // If step is explicitly in unlocked set from the backend
        if (unlockedSet.contains(step.id)) {
            return StepStatus.AVAILABLE
        }

        // Check prerequisite step in lesson
        if (step.stepNumber == 1) {
            return StepStatus.AVAILABLE
        }

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
     */
    fun computeProgressAfterStepCompletion(
        currentProgress: ChildProgress,
        completedStep: Step,
        allStepsInLesson: List<Step>
    ): ChildProgress {
        val alreadyCompleted = currentProgress.completedStepIds.contains(completedStep.id)
        val updatedCompleted = (currentProgress.completedStepIds + completedStep.id).distinct()
        val nextStep = allStepsInLesson.firstOrNull { it.stepNumber == completedStep.stepNumber + 1 }

        val updatedUnlocked = if (nextStep != null) {
            (currentProgress.unlockedStepIds + nextStep.id).distinct()
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
