package com.triplestar.learn.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

object LocaleManager {
    private const val PREFS_NAME = "triplestar_learn_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    const val LANG_ENGLISH = "en"
    const val LANG_URDU = "ur"

    private val _currentLanguage = MutableStateFlow(LANG_ENGLISH)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun init(context: Context) {
        val prefs = getPrefs(context)
        val savedLang = prefs.getString(KEY_LANGUAGE, LANG_ENGLISH) ?: LANG_ENGLISH
        _currentLanguage.value = savedLang
        applyLocale(context, savedLang)
    }

    fun setLanguage(context: Context, languageCode: String) {
        val validCode = if (languageCode == LANG_URDU) LANG_URDU else LANG_ENGLISH
        getPrefs(context).edit().putString(KEY_LANGUAGE, validCode).apply()
        _currentLanguage.value = validCode
        applyLocale(context, validCode)
    }

    fun getLayoutDirection(languageCode: String): LayoutDirection {
        return if (languageCode == LANG_URDU) LayoutDirection.Rtl else LayoutDirection.Ltr
    }

    private fun applyLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
