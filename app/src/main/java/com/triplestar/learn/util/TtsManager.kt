package com.triplestar.learn.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TtsManager {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    var isSlowMode: Boolean = false

    fun init(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.ENGLISH)
                    tts?.setSpeechRate(0.85f)
                    tts?.setPitch(1.05f)
                    isInitialized = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
                } else {
                    Log.e("TtsManager", "TTS initialization failed")
                }
            }
        }
    }

    fun speak(text: String, isUrdu: Boolean = false) {
        try {
            if (tts == null) return
            val locale = if (isUrdu) Locale("ur", "PK") else Locale.ENGLISH
            tts?.setLanguage(locale)
            tts?.setSpeechRate(if (isSlowMode) 0.55f else 0.85f)
            tts?.setPitch(1.05f)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } catch (e: Exception) {
            Log.w("TtsManager", "Error speaking text: ${e.message}")
        }
    }

    fun speakNumber(number: Int, isUrdu: Boolean) {
        val word = if (isUrdu) MathHelper.toUrduWords(number) else MathHelper.toEnglishWords(number)
        speak(word, isUrdu)
    }

    fun speakAlphabet(letter: String, isUrdu: Boolean) {
        val text = if (isUrdu) {
            "حرف $letter"
        } else {
            "Letter $letter"
        }
        speak(text, isUrdu)
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            // Ignore
        }
    }
}
