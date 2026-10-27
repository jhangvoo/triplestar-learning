package com.triplestar.learn.util

object MathHelper {

    private val URDU_DIGITS = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    private val URDU_1_TO_100 = arrayOf(
        "", "ایک", "دو", "تین", "چار", "پانچ", "چھ", "سات", "آٹھ", "نو", "دس",
        "گیارہ", "بارہ", "تیرہ", "چودہ", "پندرہ", "سولہ", "سترہ", "اٹھارہ", "انیس", "بیس",
        "اکیس", "بائیس", "تیئیس", "چوبیس", "پچیس", "چھبیس", "ستائیس", "اٹھائیس", "انتیس", "تیس",
        "اکتیس", "بتیس", "تینتیس", "چونتیس", "پینتیس", "چھتیس", "سینتیس", "اڑتیس", "انتالیس", "چالیس",
        "اکتالیس", "بیالیس", "تینتالیس", "چوالیس", "پینتالیس", "چھیالیس", "سینتالیس", "اڑتالیس", "انچاس", "پچاس",
        "اکیاون", "باون", "ترپن", "چون", "پچپن", "چھپن", "ستاون", "اٹھاون", "انسٹھ", "ساٹھ",
        "اکسٹھ", "باسٹھ", "تریسٹھ", "چونسٹھ", "پینسٹھ", "چھیاسٹھ", "سڑسٹھ", "اڑسٹھ", "انہتر", "ستر",
        "اکہتر", "بہتر", "تہتر", "چوہتر", "پچہتر", "چھہتر", "ستتر", "اٹھہتر", "اناسی", "اسی",
        "اکیاسی", "بیاسی", "تراسی", "چوراسی", "پچاسی", "چھیاسی", "ستاسی", "اٹھاسی", "نواسی", "نوے",
        "اکیانوے", "بانوے", "ترانوے", "چورانوے", "پچانوے", "چھیانوے", "ستانوے", "اٹھانوے", "ننانوے", "ایک سو"
    )

    private val URDU_HUNDREDS = arrayOf(
        "", "ایک سو", "دو سو", "تین سو", "چار سو", "پانچ سو",
        "چھ سو", "سات سو", "آٹھ سو", "نو سو"
    )

    private val ENGLISH_1_TO_19 = arrayOf(
        "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
        "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
        "Seventeen", "Eighteen", "Nineteen"
    )

    private val ENGLISH_TENS = arrayOf(
        "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    )

    private val URDU_TABLE_MULTIPLIERS = mapOf(
        1 to "ایکم",
        2 to "دونی",
        3 to "تیئے",
        4 to "چوکے",
        5 to "پنجے",
        6 to "چھکے",
        7 to "ستے",
        8 to "اٹھے",
        9 to "نم",
        10 to "دھائے"
    )

    /**
     * Converts an integer to Eastern Arabic Urdu numerals (e.g., 25 -> "۲۵", 1000 -> "۱۰۰۰").
     */
    fun toUrduDigits(number: Int): String {
        if (number == 0) return "۰"
        val isNegative = number < 0
        val numStr = Math.abs(number).toString()
        val converted = numStr.map { ch ->
            if (ch in '0'..'9') URDU_DIGITS[ch - '0'] else ch
        }.joinToString("")
        return if (isNegative) "-$converted" else converted
    }

    /**
     * Converts a number between 1 and 1000 to its complete Urdu words.
     */
    fun toUrduWords(number: Int): String {
        return when {
            number <= 0 -> "صفر"
            number <= 100 -> URDU_1_TO_100[number]
            number in 101..999 -> {
                val hundreds = number / 100
                val remainder = number % 100
                val hundredText = URDU_HUNDREDS[hundreds]
                if (remainder == 0) {
                    hundredText
                } else {
                    "$hundredText ${URDU_1_TO_100[remainder]}"
                }
            }
            number == 1000 -> "ایک ہزار"
            else -> toUrduDigits(number)
        }
    }

    /**
     * Converts a number between 1 and 1000 to English title-case words.
     */
    fun toEnglishWords(number: Int): String {
        return when {
            number <= 0 -> "Zero"
            number < 20 -> ENGLISH_1_TO_19[number]
            number in 20..99 -> {
                val tens = ENGLISH_TENS[number / 10]
                val ones = ENGLISH_1_TO_19[number % 10]
                if (ones.isNotEmpty()) "$tens $ones" else tens
            }
            number in 100..999 -> {
                val hundreds = ENGLISH_1_TO_19[number / 100] + " Hundred"
                val remainder = number % 100
                if (remainder > 0) {
                    "$hundreds ${toEnglishWords(remainder)}"
                } else {
                    hundreds
                }
            }
            number == 1000 -> "One Thousand"
            else -> number.toString()
        }
    }

    /**
     * Returns the rhythmic recitation of a multiplication row in Urdu (e.g., "دو پنجے دس").
     */
    fun getTablePhraseUrdu(table: Int, multiplier: Int, product: Int): String {
        val multiplierWord = URDU_TABLE_MULTIPLIERS[multiplier]
        val tableWord = if (table <= 100) URDU_1_TO_100[table] else toUrduDigits(table)
        val productWord = toUrduWords(product)

        return if (multiplierWord != null) {
            "$tableWord $multiplierWord $productWord"
        } else {
            "$tableWord ضرب ${toUrduWords(multiplier)} برابر $productWord"
        }
    }

    /**
     * Returns the English pronunciation for a table row (e.g., "2 times 5 equals 10").
     */
    fun getTablePhraseEnglish(table: Int, multiplier: Int, product: Int): String {
        return "$table times $multiplier equals $product"
    }
}
