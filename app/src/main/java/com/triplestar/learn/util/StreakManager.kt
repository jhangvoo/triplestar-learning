package com.triplestar.learn.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object StreakManager {
    private const val PREF_NAME = "streak_prefs"
    private const val KEY_STREAK = "streak_count"
    private const val KEY_LAST_DATE = "last_active_date"
    private const val DATE_FORMAT = "yyyy-MM-dd"

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        return sdf.format(Date())
    }

    private fun calculateDaysBetween(startDateStr: String, endDateStr: String): Long {
        return try {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
            val startDate = sdf.parse(startDateStr) ?: return -1L
            val endDate = sdf.parse(endDateStr) ?: return -1L
            val diffMs = endDate.time - startDate.time
            TimeUnit.DAYS.convert(diffMs, TimeUnit.MILLISECONDS)
        } catch (_: Exception) {
            -1L
        }
    }

    fun getStreak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastDateStr = prefs.getString(KEY_LAST_DATE, null) ?: return 0
        val todayStr = getTodayDateString()

        val daysBetween = calculateDaysBetween(lastDateStr, todayStr)

        return when {
            daysBetween == 0L -> prefs.getInt(KEY_STREAK, 0)
            daysBetween == 1L -> prefs.getInt(KEY_STREAK, 0)
            else -> 0
        }
    }

    fun updateStreak(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastDateStr = prefs.getString(KEY_LAST_DATE, null)
        val todayStr = getTodayDateString()

        if (lastDateStr == null) {
            prefs.edit().putInt(KEY_STREAK, 1).putString(KEY_LAST_DATE, todayStr).apply()
            return
        }

        val daysBetween = calculateDaysBetween(lastDateStr, todayStr)

        when {
            daysBetween == 0L -> { /* Already updated today */ }
            daysBetween == 1L -> {
                val currentStreak = prefs.getInt(KEY_STREAK, 0)
                prefs.edit().putInt(KEY_STREAK, currentStreak + 1).putString(KEY_LAST_DATE, todayStr).apply()
            }
            else -> {
                prefs.edit().putInt(KEY_STREAK, 1).putString(KEY_LAST_DATE, todayStr).apply()
            }
        }
    }
}

