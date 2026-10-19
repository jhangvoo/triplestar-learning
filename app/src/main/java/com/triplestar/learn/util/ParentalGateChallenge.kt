package com.triplestar.learn.util

import kotlin.random.Random

data class GateQuestion(
    val factor1: Int,
    val factor2: Int,
    val correctAnswer: Int
) {
    fun questionText(languageCode: String): String {
        return if (languageCode == LocaleManager.LANG_URDU) {
            "$factor1 × $factor2 کتنا ہوتا ہے؟"
        } else {
            "What is $factor1 × $factor2?"
        }
    }
}

object ParentalGateChallenge {
    // Generate a multiplication problem challenging enough for 5-14 year olds
    fun generateChallenge(): GateQuestion {
        val f1 = Random.nextInt(7, 13) // 7 to 12
        val f2 = Random.nextInt(6, 10) // 6 to 9
        return GateQuestion(
            factor1 = f1,
            factor2 = f2,
            correctAnswer = f1 * f2
        )
    }

    fun verifyAnswer(challenge: GateQuestion, userInput: String): Boolean {
        val parsed = userInput.trim().toIntOrNull() ?: return false
        return parsed == challenge.correctAnswer
    }
}
