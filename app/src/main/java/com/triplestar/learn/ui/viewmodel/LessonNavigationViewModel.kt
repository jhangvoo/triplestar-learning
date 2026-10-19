package com.triplestar.learn.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Lesson
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepStatus
import com.triplestar.learn.data.repository.CurriculumRepository
import com.triplestar.learn.data.repository.ProgressRepository
import com.triplestar.learn.util.StepLockEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonNavigationViewModel(
    private val context: Context,
    private val childId: String,
    private val curriculumRepository: CurriculumRepository = CurriculumRepository(),
    private val progressRepository: ProgressRepository = ProgressRepository(context)
) : ViewModel() {

    private val _selectedSubjectId = MutableStateFlow("english")
    val selectedSubjectId: StateFlow<String> = _selectedSubjectId.asStateFlow()

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _subjectProgress = MutableStateFlow<ChildProgress?>(null)
    val subjectProgress: StateFlow<ChildProgress?> = _subjectProgress.asStateFlow()

    init {
        loadSubject("english")
    }

    fun selectSubject(subjectId: String) {
        _selectedSubjectId.value = subjectId
        loadSubject(subjectId)
    }

    private fun loadSubject(subjectId: String) {
        val subjectLessons = curriculumRepository.getLessonsForSubject(subjectId)
        _lessons.value = subjectLessons

        viewModelScope.launch {
            progressRepository.observeProgress(childId, subjectId).collect { progress ->
                _subjectProgress.value = progress
            }
        }
    }

    /**
     * Determines whether a specific lesson is unlocked based on completion of preceding lessons/steps.
     */
    fun isLessonUnlocked(lesson: Lesson, allLessons: List<Lesson>, progress: ChildProgress?): Boolean {
        val lessonIndex = allLessons.indexOf(lesson)
        if (lessonIndex <= 0) return true // First lesson is always unlocked

        // Check if previous lesson is completed or has at least one step completed
        val prevLesson = allLessons[lessonIndex - 1]
        val prevSteps = curriculumRepository.getStepsForLesson(prevLesson.id)
        val completedSteps = progress?.completedStepIds ?: emptySet()

        // Unlocked if any step of the previous lesson is completed
        return prevSteps.any { completedSteps.contains(it.id) }
    }

    /**
     * Resolves step status adhering strictly to sequential step locking.
     */
    fun getStepStatus(step: Step, allStepsInLesson: List<Step>, progress: ChildProgress?): StepStatus {
        return StepLockEngine.resolveStepStatus(step, allStepsInLesson, progress)
    }

    /**
     * Finds the next recommended step for the learner to continue their curriculum path.
     */
    fun getNextRecommendedStep(subjectId: String, progress: ChildProgress?): Pair<Lesson, Step>? {
        val subjectLessons = curriculumRepository.getLessonsForSubject(subjectId)
        for (lesson in subjectLessons) {
            val steps = curriculumRepository.getStepsForLesson(lesson.id)
            for (step in steps) {
                val status = StepLockEngine.resolveStepStatus(step, steps, progress)
                if (status == StepStatus.AVAILABLE || status == StepStatus.IN_PROGRESS) {
                    return Pair(lesson, step)
                }
            }
        }
        // Fallback to first lesson & first step if all completed
        val firstLesson = subjectLessons.firstOrNull() ?: return null
        val firstStep = curriculumRepository.getStepsForLesson(firstLesson.id).firstOrNull() ?: return null
        return Pair(firstLesson, firstStep)
    }
}
