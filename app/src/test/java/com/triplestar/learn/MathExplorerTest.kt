package com.triplestar.learn

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.triplestar.learn.ui.components.ADDITION_PUZZLES
import com.triplestar.learn.ui.components.MATH_NUMBERS
import com.triplestar.learn.ui.components.MATH_QUIZ_QUESTIONS
import com.triplestar.learn.ui.components.SUBTRACTION_PUZZLES
import com.triplestar.learn.util.MathHelper
import com.triplestar.learn.util.MathProgressManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MathExplorerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        MathProgressManager.init(context)
        MathProgressManager.resetProgress(context)
    }

    @Test
    fun `verify complete 1 to 20 numbers sequence with visual emojis`() {
        assertEquals("Should have exactly 20 numbers", 20, MATH_NUMBERS.size)
        MATH_NUMBERS.forEachIndexed { index, item ->
            val expectedValue = index + 1
            assertEquals("Number value must match sequential 1 to 20", expectedValue, item.value)
            assertEquals("Digit string must match value", "$expectedValue", item.digitEn)
            assertTrue("DigitUr must not be blank", item.digitUr.isNotBlank())
            assertTrue("NameEn must not be blank", item.nameEn.isNotBlank())
            assertTrue("NameUr must not be blank", item.nameUr.isNotBlank())
            assertTrue("RomanUr must not be blank", item.romanUr.isNotBlank())
            assertTrue("Emoji must not be blank", item.emoji.isNotBlank())
            assertTrue("ItemNameEn must not be blank", item.itemNameEn.isNotBlank())
            assertTrue("ItemNameUr must not be blank", item.itemNameUr.isNotBlank())
            assertFalse("ColorHex must not be 0", item.colorHex == 0L)
        }
    }

    @Test
    fun `verify Eastern Arabic Urdu digits generator up to 1000`() {
        assertEquals("۰", MathHelper.toUrduDigits(0))
        assertEquals("۱", MathHelper.toUrduDigits(1))
        assertEquals("۵", MathHelper.toUrduDigits(5))
        assertEquals("۱۰", MathHelper.toUrduDigits(10))
        assertEquals("۲۵", MathHelper.toUrduDigits(25))
        assertEquals("۱۰۰", MathHelper.toUrduDigits(100))
        assertEquals("۲۵۰", MathHelper.toUrduDigits(250))
        assertEquals("۹۹۹", MathHelper.toUrduDigits(999))
        assertEquals("۱۰۰۰", MathHelper.toUrduDigits(1000))
    }

    @Test
    fun `verify complete Urdu words up to 1000`() {
        assertEquals("ایک", MathHelper.toUrduWords(1))
        assertEquals("دس", MathHelper.toUrduWords(10))
        assertEquals("پچیس", MathHelper.toUrduWords(25))
        assertEquals("پچاس", MathHelper.toUrduWords(50))
        assertEquals("ایک سو", MathHelper.toUrduWords(100))
        assertEquals("ایک سو پانچ", MathHelper.toUrduWords(105))
        assertEquals("دو سو پچاس", MathHelper.toUrduWords(250))
        assertEquals("پانچ سو", MathHelper.toUrduWords(500))
        assertEquals("نو سو ننانوے", MathHelper.toUrduWords(999))
        assertEquals("ایک ہزار", MathHelper.toUrduWords(1000))
    }

    @Test
    fun `verify English words converter up to 1000`() {
        assertEquals("One", MathHelper.toEnglishWords(1))
        assertEquals("Ten", MathHelper.toEnglishWords(10))
        assertEquals("Twenty Five", MathHelper.toEnglishWords(25))
        assertEquals("Fifty", MathHelper.toEnglishWords(50))
        assertEquals("One Hundred", MathHelper.toEnglishWords(100))
        assertEquals("One Hundred Five", MathHelper.toEnglishWords(105))
        assertEquals("Two Hundred Fifty", MathHelper.toEnglishWords(250))
        assertEquals("Five Hundred", MathHelper.toEnglishWords(500))
        assertEquals("Nine Hundred Ninety Nine", MathHelper.toEnglishWords(999))
        assertEquals("One Thousand", MathHelper.toEnglishWords(1000))
    }

    @Test
    fun `verify multiplication table phrases in Urdu and English`() {
        // Table of 2
        val phraseUrdu2x1 = MathHelper.getTablePhraseUrdu(2, 1, 2)
        assertTrue("Should contain ایکم", phraseUrdu2x1.contains("ایکم"))

        val phraseUrdu2x5 = MathHelper.getTablePhraseUrdu(2, 5, 10)
        assertEquals("دو پنجے دس", phraseUrdu2x5)

        val phraseEn2x5 = MathHelper.getTablePhraseEnglish(2, 5, 10)
        assertEquals("2 times 5 equals 10", phraseEn2x5)

        // Table of 3
        val phraseUrdu3x4 = MathHelper.getTablePhraseUrdu(3, 4, 12)
        assertEquals("تین چوکے بارہ", phraseUrdu3x4)
    }

    @Test
    fun `verify visual addition puzzles calculations and options`() {
        assertTrue("Should have addition puzzles", ADDITION_PUZZLES.isNotEmpty())
        ADDITION_PUZZLES.forEach { puzzle ->
            assertTrue("Should be addition puzzle", puzzle.isAddition)
            assertTrue("Operand1 must be positive", puzzle.operand1 > 0)
            assertTrue("Operand2 must be positive", puzzle.operand2 > 0)
            assertTrue("Emoji must not be blank", puzzle.emoji.isNotBlank())
            assertTrue("ItemNameEn must not be blank", puzzle.itemNameEn.isNotBlank())
            assertTrue("ItemNameUr must not be blank", puzzle.itemNameUr.isNotBlank())

            assertTrue("Options must have at least 3 choices", puzzle.options.size >= 3)
            assertTrue("CorrectIndex must be within options range", puzzle.correctIndex in puzzle.options.indices)

            val expectedSum = puzzle.operand1 + puzzle.operand2
            val actualAnswer = puzzle.options[puzzle.correctIndex]
            assertEquals("Calculated sum must match target option", expectedSum, actualAnswer)

            assertTrue("ExplanationEn must not be blank", puzzle.explanationEn.isNotBlank())
            assertTrue("ExplanationUr must not be blank", puzzle.explanationUr.isNotBlank())
        }
    }

    @Test
    fun `verify visual subtraction puzzles calculations and options`() {
        assertTrue("Should have subtraction puzzles", SUBTRACTION_PUZZLES.isNotEmpty())
        SUBTRACTION_PUZZLES.forEach { puzzle ->
            assertFalse("Should not be addition puzzle", puzzle.isAddition)
            assertTrue("Operand1 must be positive", puzzle.operand1 > 0)
            assertTrue("Operand2 must be positive", puzzle.operand2 > 0)
            assertTrue("Operand1 must be >= Operand2 for child subtraction", puzzle.operand1 >= puzzle.operand2)
            assertTrue("Emoji must not be blank", puzzle.emoji.isNotBlank())

            assertTrue("Options must have at least 3 choices", puzzle.options.size >= 3)
            assertTrue("CorrectIndex must be within options range", puzzle.correctIndex in puzzle.options.indices)

            val expectedDifference = puzzle.operand1 - puzzle.operand2
            val actualAnswer = puzzle.options[puzzle.correctIndex]
            assertEquals("Calculated difference must match target option", expectedDifference, actualAnswer)

            assertTrue("ExplanationEn must not be blank", puzzle.explanationEn.isNotBlank())
            assertTrue("ExplanationUr must not be blank", puzzle.explanationUr.isNotBlank())
        }
    }

    @Test
    fun `verify math quiz questions bilingual consistency and answers`() {
        assertTrue("Should have math quiz questions", MATH_QUIZ_QUESTIONS.isNotEmpty())
        MATH_QUIZ_QUESTIONS.forEach { q ->
            assertTrue("QuestionEn must not be blank", q.questionEn.isNotBlank())
            assertTrue("QuestionUr must not be blank", q.questionUr.isNotBlank())
            assertTrue("Emoji must not be blank", q.emoji.isNotBlank())
            assertEquals("English and Urdu option counts must match", q.optionsEn.size, q.optionsUr.size)
            assertTrue("Must have at least 3 options", q.optionsEn.size >= 3)
            assertTrue("CorrectIndex must be in bounds", q.correctIndex in q.optionsEn.indices)
            assertTrue("ExplanationEn must not be blank", q.explanationEn.isNotBlank())
            assertTrue("ExplanationUr must not be blank", q.explanationUr.isNotBlank())
        }
    }

    @Test
    fun `verify MathProgressManager persistent data operations`() {
        val initial = MathProgressManager.state.value
        assertEquals(0, initial.totalStars)
        assertEquals(0, initial.totalScore)
        assertTrue(initial.solvedQuizIds.isEmpty())

        // Record a quiz completion
        MathProgressManager.recordQuizCompletion(context, "quiz_1", starsEarned = 1)
        val afterQuiz = MathProgressManager.state.value
        assertEquals(1, afterQuiz.totalStars)
        assertEquals(10, afterQuiz.totalScore)
        assertTrue(afterQuiz.solvedQuizIds.contains("quiz_1"))

        // Record a puzzle completion
        MathProgressManager.recordPuzzleCompletion(context, "add_1", starsEarned = 1)
        val afterPuzzle = MathProgressManager.state.value
        assertEquals(2, afterPuzzle.totalStars)
        assertEquals(20, afterPuzzle.totalScore)
        assertTrue(afterPuzzle.solvedPuzzleIds.contains("add_1"))

        // Record number counting
        MathProgressManager.recordNumberCounted(context, 7)
        val afterNumber = MathProgressManager.state.value
        assertEquals(3, afterNumber.totalStars)
        assertTrue(afterNumber.completedNumbers.contains(7))

        // Record table mastered
        MathProgressManager.recordTableMastered(context, 2)
        val afterTable = MathProgressManager.state.value
        assertEquals(5, afterTable.totalStars)
        assertTrue(afterTable.masteredTables.contains(2))

        // Re-initialize from persistent SharedPreferences
        MathProgressManager.init(context)
        val reloaded = MathProgressManager.state.value
        assertEquals(5, reloaded.totalStars)
        assertTrue(reloaded.solvedQuizIds.contains("quiz_1"))
        assertTrue(reloaded.solvedPuzzleIds.contains("add_1"))
        assertTrue(reloaded.completedNumbers.contains(7))
        assertTrue(reloaded.masteredTables.contains(2))
    }
}
