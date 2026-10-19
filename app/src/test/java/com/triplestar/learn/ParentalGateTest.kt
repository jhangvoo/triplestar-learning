package com.triplestar.learn

import com.triplestar.learn.util.ParentalGateChallenge
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ParentalGateTest {

    @Test
    fun `generated challenge has valid multiplication factors`() {
        val challenge = ParentalGateChallenge.generateChallenge()
        assertTrue("Factor 1 should be between 7 and 12", challenge.factor1 in 7..12)
        assertTrue("Factor 2 should be between 6 and 9", challenge.factor2 in 6..9)
        assertEquals(challenge.factor1 * challenge.factor2, challenge.correctAnswer)
    }

    @Test
    fun `verifyAnswer accepts correct answer`() {
        val challenge = ParentalGateChallenge.generateChallenge()
        val correctString = challenge.correctAnswer.toString()
        assertTrue(ParentalGateChallenge.verifyAnswer(challenge, correctString))
        assertTrue(ParentalGateChallenge.verifyAnswer(challenge, "  $correctString  "))
    }

    @Test
    fun `verifyAnswer rejects incorrect answers and garbage input`() {
        val challenge = ParentalGateChallenge.generateChallenge()
        assertFalse(ParentalGateChallenge.verifyAnswer(challenge, (challenge.correctAnswer + 1).toString()))
        assertFalse(ParentalGateChallenge.verifyAnswer(challenge, "abc"))
        assertFalse(ParentalGateChallenge.verifyAnswer(challenge, ""))
        assertFalse(ParentalGateChallenge.verifyAnswer(challenge, "-1"))
    }
}
