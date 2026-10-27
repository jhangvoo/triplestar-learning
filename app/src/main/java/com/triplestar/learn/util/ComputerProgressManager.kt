package com.triplestar.learn.util

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ComputerProgressState(
    val solvedQuizIds: Set<String> = emptySet(),
    val solvedPuzzleIds: Set<String> = emptySet(),
    val exploredComponents: Set<String> = emptySet(),
    val completedCodingLevels: Set<Int> = emptySet(),
    val earnedSafetyBadges: Set<String> = emptySet(),
    val totalStars: Int = 0,
    val totalScore: Int = 0
)

object ComputerProgressManager {
    private const val PREFS_NAME = "triplestar_computer_progress"
    private const val KEY_SOLVED_QUIZZES = "comp_solved_quiz_ids"
    private const val KEY_SOLVED_PUZZLES = "comp_solved_puzzle_ids"
    private const val KEY_EXPLORED_COMPONENTS = "comp_explored_components"
    private const val KEY_COMPLETED_CODING_LEVELS = "comp_completed_coding_levels"
    private const val KEY_EARNED_SAFETY_BADGES = "comp_earned_safety_badges"
    private const val KEY_TOTAL_STARS = "comp_total_stars"
    private const val KEY_TOTAL_SCORE = "comp_total_score"

    private val _state = MutableStateFlow(ComputerProgressState())
    val state: StateFlow<ComputerProgressState> = _state.asStateFlow()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val quizzes = prefs.getStringSet(KEY_SOLVED_QUIZZES, emptySet()) ?: emptySet()
        val puzzles = prefs.getStringSet(KEY_SOLVED_PUZZLES, emptySet()) ?: emptySet()
        val components = prefs.getStringSet(KEY_EXPLORED_COMPONENTS, emptySet()) ?: emptySet()
        val levels = prefs.getStringSet(KEY_COMPLETED_CODING_LEVELS, emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val badges = prefs.getStringSet(KEY_EARNED_SAFETY_BADGES, emptySet()) ?: emptySet()
        val stars = prefs.getInt(KEY_TOTAL_STARS, 0)
        val score = prefs.getInt(KEY_TOTAL_SCORE, 0)

        _state.value = ComputerProgressState(
            solvedQuizIds = quizzes,
            solvedPuzzleIds = puzzles,
            exploredComponents = components,
            completedCodingLevels = levels,
            earnedSafetyBadges = badges,
            totalStars = stars,
            totalScore = score
        )
    }

    fun recordQuizCompletion(context: Context, quizId: String, starsEarned: Int = 1) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.solvedQuizIds.contains(quizId)
        val newQuizzes = current.solvedQuizIds + quizId
        val addStars = if (isFirstTime) starsEarned else 0
        val newStars = current.totalStars + addStars
        val newScore = current.totalScore + (addStars * 10)

        prefs.edit()
            .putStringSet(KEY_SOLVED_QUIZZES, newQuizzes)
            .putInt(KEY_TOTAL_STARS, newStars)
            .putInt(KEY_TOTAL_SCORE, newScore)
            .apply()

        _state.value = current.copy(
            solvedQuizIds = newQuizzes,
            totalStars = newStars,
            totalScore = newScore
        )
    }

    fun recordCodingLevelCompletion(context: Context, level: Int, starsEarned: Int = 2) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.completedCodingLevels.contains(level)
        val newLevels = current.completedCodingLevels + level
        val addStars = if (isFirstTime) starsEarned else 0
        val newStars = current.totalStars + addStars
        val newScore = current.totalScore + (addStars * 10)

        prefs.edit()
            .putStringSet(KEY_COMPLETED_CODING_LEVELS, newLevels.map { it.toString() }.toSet())
            .putInt(KEY_TOTAL_STARS, newStars)
            .putInt(KEY_TOTAL_SCORE, newScore)
            .apply()

        _state.value = current.copy(
            completedCodingLevels = newLevels,
            totalStars = newStars,
            totalScore = newScore
        )
    }

    fun recordComponentExplored(context: Context, componentId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.exploredComponents.contains(componentId)
        val newComponents = current.exploredComponents + componentId
        val addStars = if (isFirstTime) 1 else 0
        val newStars = current.totalStars + addStars
        val newScore = current.totalScore + (addStars * 5)

        prefs.edit()
            .putStringSet(KEY_EXPLORED_COMPONENTS, newComponents)
            .putInt(KEY_TOTAL_STARS, newStars)
            .putInt(KEY_TOTAL_SCORE, newScore)
            .apply()

        _state.value = current.copy(
            exploredComponents = newComponents,
            totalStars = newStars,
            totalScore = newScore
        )
    }

    fun recordSafetyBadgeEarned(context: Context, badgeId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.earnedSafetyBadges.contains(badgeId)
        val newBadges = current.earnedSafetyBadges + badgeId
        val addStars = if (isFirstTime) 2 else 0
        val newStars = current.totalStars + addStars
        val newScore = current.totalScore + (addStars * 10)

        prefs.edit()
            .putStringSet(KEY_EARNED_SAFETY_BADGES, newBadges)
            .putInt(KEY_TOTAL_STARS, newStars)
            .putInt(KEY_TOTAL_SCORE, newScore)
            .apply()

        _state.value = current.copy(
            earnedSafetyBadges = newBadges,
            totalStars = newStars,
            totalScore = newScore
        )
    }

    fun resetProgress(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        _state.value = ComputerProgressState()
    }
}
