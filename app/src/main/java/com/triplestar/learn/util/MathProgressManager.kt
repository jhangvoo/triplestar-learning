package com.triplestar.learn.util

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MathProgressState(
    val solvedQuizIds: Set<String> = emptySet(),
    val solvedPuzzleIds: Set<String> = emptySet(),
    val completedNumbers: Set<Int> = emptySet(),
    val masteredTables: Set<Int> = emptySet(),
    val totalStars: Int = 0,
    val totalScore: Int = 0
)

object MathProgressManager {
    private const val PREFS_NAME = "triplestar_math_progress"
    private const val KEY_SOLVED_QUIZZES = "solved_quiz_ids"
    private const val KEY_SOLVED_PUZZLES = "solved_puzzle_ids"
    private const val KEY_COMPLETED_NUMBERS = "completed_numbers"
    private const val KEY_MASTERED_TABLES = "mastered_tables"
    private const val KEY_TOTAL_STARS = "total_stars"
    private const val KEY_TOTAL_SCORE = "total_score"

    private val _state = MutableStateFlow(MathProgressState())
    val state: StateFlow<MathProgressState> = _state.asStateFlow()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val quizzes = prefs.getStringSet(KEY_SOLVED_QUIZZES, emptySet()) ?: emptySet()
        val puzzles = prefs.getStringSet(KEY_SOLVED_PUZZLES, emptySet()) ?: emptySet()
        val numbers = prefs.getStringSet(KEY_COMPLETED_NUMBERS, emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val tables = prefs.getStringSet(KEY_MASTERED_TABLES, emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val stars = prefs.getInt(KEY_TOTAL_STARS, 0)
        val score = prefs.getInt(KEY_TOTAL_SCORE, 0)

        _state.value = MathProgressState(
            solvedQuizIds = quizzes,
            solvedPuzzleIds = puzzles,
            completedNumbers = numbers,
            masteredTables = tables,
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

    fun recordPuzzleCompletion(context: Context, puzzleId: String, starsEarned: Int = 1) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.solvedPuzzleIds.contains(puzzleId)
        val newPuzzles = current.solvedPuzzleIds + puzzleId
        val addStars = if (isFirstTime) starsEarned else 0
        val newStars = current.totalStars + addStars
        val newScore = current.totalScore + (addStars * 10)

        prefs.edit()
            .putStringSet(KEY_SOLVED_PUZZLES, newPuzzles)
            .putInt(KEY_TOTAL_STARS, newStars)
            .putInt(KEY_TOTAL_SCORE, newScore)
            .apply()

        _state.value = current.copy(
            solvedPuzzleIds = newPuzzles,
            totalStars = newStars,
            totalScore = newScore
        )
    }

    fun recordNumberCounted(context: Context, number: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.completedNumbers.contains(number)
        val newNumbers = current.completedNumbers + number
        val addStars = if (isFirstTime) 1 else 0
        val newStars = current.totalStars + addStars

        prefs.edit()
            .putStringSet(KEY_COMPLETED_NUMBERS, newNumbers.map { it.toString() }.toSet())
            .putInt(KEY_TOTAL_STARS, newStars)
            .apply()

        _state.value = current.copy(
            completedNumbers = newNumbers,
            totalStars = newStars
        )
    }

    fun recordTableMastered(context: Context, table: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = _state.value
        val isFirstTime = !current.masteredTables.contains(table)
        val newTables = current.masteredTables + table
        val addStars = if (isFirstTime) 2 else 0
        val newStars = current.totalStars + addStars

        prefs.edit()
            .putStringSet(KEY_MASTERED_TABLES, newTables.map { it.toString() }.toSet())
            .putInt(KEY_TOTAL_STARS, newStars)
            .apply()

        _state.value = current.copy(
            masteredTables = newTables,
            totalStars = newStars
        )
    }

    fun resetProgress(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        _state.value = MathProgressState()
    }
}
