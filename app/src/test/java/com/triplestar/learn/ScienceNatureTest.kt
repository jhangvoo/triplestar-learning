package com.triplestar.learn

import com.triplestar.learn.ui.components.SCIENCE_TOPICS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScienceNatureTest {

    @Test
    fun `verify all 4 science topics are configured`() {
        assertEquals("Should have 4 core science topics", 4, SCIENCE_TOPICS.size)
        val topicIds = SCIENCE_TOPICS.map { it.id }.toSet()
        assertTrue("Animals topic should exist", topicIds.contains("animals"))
        assertTrue("Plants topic should exist", topicIds.contains("plants"))
        assertTrue("Weather topic should exist", topicIds.contains("weather"))
        assertTrue("Space topic should exist", topicIds.contains("space"))
    }

    @Test
    fun `verify science topics have complete offline bilingual metadata`() {
        SCIENCE_TOPICS.forEach { topic ->
            assertTrue("Topic id must not be blank", topic.id.isNotBlank())
            assertTrue("Topic nameEn must not be blank", topic.nameEn.isNotBlank())
            assertTrue("Topic nameUr must not be blank", topic.nameUr.isNotBlank())
            assertTrue("Topic emoji must not be blank", topic.emoji.isNotBlank())
            assertFalse("Topic colorHex must not be 0", topic.colorHex == 0L)
            assertTrue("Topic overviewEn must not be blank", topic.overviewEn.isNotBlank())
            assertTrue("Topic overviewUr must not be blank", topic.overviewUr.isNotBlank())
            assertTrue("Topic must have fun facts", topic.facts.isNotEmpty())
        }
    }

    @Test
    fun `verify fun facts have valid bilingual details and emojis`() {
        SCIENCE_TOPICS.forEach { topic ->
            topic.facts.forEach { fact ->
                assertTrue("Fact titleEn must not be blank", fact.titleEn.isNotBlank())
                assertTrue("Fact titleUr must not be blank", fact.titleUr.isNotBlank())
                assertTrue("Fact detailEn must not be blank", fact.detailEn.isNotBlank())
                assertTrue("Fact detailUr must not be blank", fact.detailUr.isNotBlank())
                assertTrue("Fact emoji must not be blank", fact.emoji.isNotBlank())
            }
        }
    }

    @Test
    fun `verify interactive mini-quiz questions and valid correct indices`() {
        SCIENCE_TOPICS.forEach { topic ->
            val quiz = topic.quiz
            assertNotNull("Topic ${topic.id} must have a quiz", quiz)
            assertTrue("Quiz questionEn must not be blank", quiz.questionEn.isNotBlank())
            assertTrue("Quiz questionUr must not be blank", quiz.questionUr.isNotBlank())
            assertEquals("English and Urdu option counts must match", quiz.optionsEn.size, quiz.optionsUr.size)
            assertTrue("Quiz options must have at least 2 choices", quiz.optionsEn.size >= 2)
            assertTrue("correctIndex must be in bounds", quiz.correctIndex in quiz.optionsEn.indices)
            assertTrue("explanationEn must not be blank", quiz.explanationEn.isNotBlank())
            assertTrue("explanationUr must not be blank", quiz.explanationUr.isNotBlank())
        }
    }
}
