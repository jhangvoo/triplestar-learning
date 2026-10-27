package com.triplestar.learn

import com.triplestar.learn.ui.components.URDU_ALPHABET_LIST
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UrduAlphabetTest {

    @Test
    fun `verify all 37 Urdu letters are present from Alif to Yae`() {
        assertEquals("Urdu alphabet should contain all 37 letters", 37, URDU_ALPHABET_LIST.size)
        assertEquals("First letter should be Alif", "ا", URDU_ALPHABET_LIST.first().letter)
        assertEquals("First letter name should be Alif", "الف", URDU_ALPHABET_LIST.first().nameUr)
        assertEquals("Last letter should be Badi Yae", "ے", URDU_ALPHABET_LIST.last().letter)
        assertEquals("Last letter name should be Badi Yae", "بڑی یے", URDU_ALPHABET_LIST.last().nameUr)
    }

    @Test
    fun `verify every Urdu letter has complete offline content and illustrations`() {
        URDU_ALPHABET_LIST.forEach { item ->
            assertTrue("Letter '${item.letter}' must not be blank", item.letter.isNotBlank())
            assertTrue("Urdu name for '${item.letter}' must not be blank", item.nameUr.isNotBlank())
            assertTrue("English name for '${item.letter}' must not be blank", item.nameEn.isNotBlank())
            assertTrue("Urdu word for '${item.letter}' must not be blank", item.wordUr.isNotBlank())
            assertTrue("English word for '${item.letter}' must not be blank", item.wordEn.isNotBlank())
            assertTrue("Emoji illustration for '${item.letter}' must not be blank", item.emoji.isNotBlank())
            assertTrue("Sound description for '${item.letter}' must not be blank", item.soundPhonics.isNotBlank())
            assertTrue("Urdu rhyme for '${item.letter}' must not be blank", item.rhymeUr.isNotBlank())
            assertFalse("Color hex should not be 0", item.colorHex == 0L)
        }
    }

    @Test
    fun `verify no duplicate letters in Urdu alphabet list`() {
        val letterSet = URDU_ALPHABET_LIST.map { it.letter }.toSet()
        assertEquals("All letters in alphabet list must be unique", URDU_ALPHABET_LIST.size, letterSet.size)
    }

    @Test
    fun `verify essential landmark letters exist`() {
        val letterMap = URDU_ALPHABET_LIST.associateBy { it.letter }
        assertNotNull("Letter Bay (ب) should exist", letterMap["ب"])
        assertEquals("بلی", letterMap["ب"]?.wordUr)
        assertNotNull("Letter Jeem (ج) should exist", letterMap["ج"])
        assertEquals("جہاز", letterMap["ج"]?.wordUr)
        assertNotNull("Letter Daal (د) should exist", letterMap["د"])
        assertEquals("درخت", letterMap["د"]?.wordUr)
        assertNotNull("Letter Seen (س) should exist", letterMap["س"])
        assertEquals("سورج", letterMap["س"]?.wordUr)
        assertNotNull("Letter Kaaf (ک) should exist", letterMap["ک"])
        assertEquals("کتاب", letterMap["ک"]?.wordUr)
        assertNotNull("Letter Meem (م) should exist", letterMap["م"])
        assertEquals("مور", letterMap["م"]?.wordUr)
    }
}
