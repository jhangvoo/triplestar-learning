package com.triplestar.learn

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.triplestar.learn.ui.components.COMPUTER_PARTS
import com.triplestar.learn.ui.components.CYBER_SCENARIOS
import com.triplestar.learn.ui.components.LOGIC_QUIZZES
import com.triplestar.learn.ui.components.ROVER_LEVELS
import com.triplestar.learn.ui.components.TechCategory
import com.triplestar.learn.util.ComputerProgressManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ComputerLogicTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        ComputerProgressManager.init(context)
        ComputerProgressManager.resetProgress(context)
    }

    @Test
    fun `verify computer parts datasets completeness and valid categorizations`() {
        assertTrue("Must have at least 8 essential hardware/software parts", COMPUTER_PARTS.size >= 8)

        val categories = COMPUTER_PARTS.map { it.category }.toSet()
        assertTrue("Must include Hardware category", categories.contains(TechCategory.HARDWARE))
        assertTrue("Must include Software category", categories.contains(TechCategory.SOFTWARE))
        assertTrue("Must include Input/Output category", categories.contains(TechCategory.INPUT_OUTPUT))

        COMPUTER_PARTS.forEach { part ->
            assertTrue("Part id must not be blank", part.id.isNotBlank())
            assertTrue("nameEn must not be blank for ${part.id}", part.nameEn.isNotBlank())
            assertTrue("nameUr must not be blank for ${part.id}", part.nameUr.isNotBlank())
            assertTrue("roleEn must not be blank for ${part.id}", part.roleEn.isNotBlank())
            assertTrue("roleUr must not be blank for ${part.id}", part.roleUr.isNotBlank())
            assertTrue("detailEn must not be blank for ${part.id}", part.detailEn.isNotBlank())
            assertTrue("detailUr must not be blank for ${part.id}", part.detailUr.isNotBlank())
            assertTrue("kidAnalogyEn must not be blank for ${part.id}", part.kidAnalogyEn.isNotBlank())
            assertTrue("kidAnalogyUr must not be blank for ${part.id}", part.kidAnalogyUr.isNotBlank())
            assertTrue("emoji must not be blank for ${part.id}", part.emoji.isNotBlank())
        }
    }

    @Test
    fun `verify rover grid levels geometry and progression`() {
        assertEquals("Must have 4 progressive coding levels", 4, ROVER_LEVELS.size)

        ROVER_LEVELS.forEachIndexed { index, level ->
            assertEquals("Level number must match 1-based index", index + 1, level.levelNumber)
            assertTrue("StartX must be in 0..3 grid", level.startX in 0..3)
            assertTrue("StartY must be in 0..3 grid", level.startY in 0..3)
            assertTrue("TargetX must be in 0..3 grid", level.targetX in 0..3)
            assertTrue("TargetY must be in 0..3 grid", level.targetY in 0..3)
            assertNotEquals("Start and Target must be distinct", Pair(level.startX, level.startY), Pair(level.targetX, level.targetY))

            level.obstacles.forEach { obstacle ->
                assertTrue("Obstacle X must be in 0..3 grid", obstacle.first in 0..3)
                assertTrue("Obstacle Y must be in 0..3 grid", obstacle.second in 0..3)
                assertNotEquals("Obstacle cannot be on start", Pair(level.startX, level.startY), obstacle)
                assertNotEquals("Obstacle cannot be on target", Pair(level.targetX, level.targetY), obstacle)
            }

            assertTrue("Level titleEn must not be blank", level.titleEn.isNotBlank())
            assertTrue("Level titleUr must not be blank", level.titleUr.isNotBlank())
            assertTrue("Level targetGoalEn must not be blank", level.targetGoalEn.isNotBlank())
            assertTrue("Level targetGoalUr must not be blank", level.targetGoalUr.isNotBlank())
            assertTrue("Available actions must not be empty", level.availableActions.isNotEmpty())
        }
    }

    @Test
    fun `verify cyber safety scenarios and badge coverage`() {
        assertTrue("Must have at least 4 cyber safety scenarios", CYBER_SCENARIOS.size >= 4)

        CYBER_SCENARIOS.forEach { scenario ->
            assertTrue("Scenario id must not be blank", scenario.id.isNotBlank())
            assertTrue("badgeId must not be blank", scenario.badgeId.isNotBlank())
            assertTrue("badgeTitleEn must not be blank", scenario.badgeTitleEn.isNotBlank())
            assertTrue("badgeTitleUr must not be blank", scenario.badgeTitleUr.isNotBlank())
            assertTrue("badgeEmoji must not be blank", scenario.badgeEmoji.isNotBlank())
            assertTrue("questionEn must not be blank", scenario.questionEn.isNotBlank())
            assertTrue("questionUr must not be blank", scenario.questionUr.isNotBlank())
            assertTrue("optionSafeEn must not be blank", scenario.optionSafeEn.isNotBlank())
            assertTrue("optionSafeUr must not be blank", scenario.optionSafeUr.isNotBlank())
            assertTrue("optionUnsafeEn must not be blank", scenario.optionUnsafeEn.isNotBlank())
            assertTrue("optionUnsafeUr must not be blank", scenario.optionUnsafeUr.isNotBlank())
            assertTrue("explanationEn must not be blank", scenario.explanationEn.isNotBlank())
            assertTrue("explanationUr must not be blank", scenario.explanationUr.isNotBlank())
            assertTrue("tipEn must not be blank", scenario.tipEn.isNotBlank())
            assertTrue("tipUr must not be blank", scenario.tipUr.isNotBlank())
        }
    }

    @Test
    fun `verify logic quizzes options and answer indices`() {
        assertTrue("Must have multiple logic quizzes", LOGIC_QUIZZES.size >= 4)

        LOGIC_QUIZZES.forEach { quiz ->
            assertTrue("Quiz id must not be blank", quiz.id.isNotBlank())
            assertTrue("titleEn must not be blank", quiz.titleEn.isNotBlank())
            assertTrue("titleUr must not be blank", quiz.titleUr.isNotBlank())
            assertTrue("questionEn must not be blank", quiz.questionEn.isNotBlank())
            assertTrue("questionUr must not be blank", quiz.questionUr.isNotBlank())
            assertTrue("explanationEn must not be blank", quiz.explanationEn.isNotBlank())
            assertTrue("explanationUr must not be blank", quiz.explanationUr.isNotBlank())

            assertTrue("Must have at least 2 English options", quiz.optionsEn.size >= 2)
            assertTrue("Must have matching count of Urdu options", quiz.optionsUr.size == quiz.optionsEn.size)
            assertTrue("CorrectIndex must be within options range", quiz.correctIndex in quiz.optionsEn.indices)
        }
    }

    @Test
    fun `verify ComputerProgressManager state transitions and persistence`() {
        val initial = ComputerProgressManager.state.value
        assertEquals("Initial totalStars must be 0", 0, initial.totalStars)
        assertEquals("Initial totalScore must be 0", 0, initial.totalScore)
        assertTrue("Initial quizzes must be empty", initial.solvedQuizIds.isEmpty())
        assertTrue("Initial levels must be empty", initial.completedCodingLevels.isEmpty())
        assertTrue("Initial badges must be empty", initial.earnedSafetyBadges.isEmpty())
        assertTrue("Initial components must be empty", initial.exploredComponents.isEmpty())

        // 1. Record Quiz completion
        ComputerProgressManager.recordQuizCompletion(context, "binary_counting", starsEarned = 1)
        var state = ComputerProgressManager.state.value
        assertTrue("Quiz must be recorded", state.solvedQuizIds.contains("binary_counting"))
        assertEquals("Total stars must be 1", 1, state.totalStars)
        assertEquals("Total XP must be 10", 10, state.totalScore)

        // Duplicate quiz completion should not grant duplicate stars
        ComputerProgressManager.recordQuizCompletion(context, "binary_counting", starsEarned = 1)
        state = ComputerProgressManager.state.value
        assertEquals("Stars must remain 1 on duplicate solve", 1, state.totalStars)
        assertEquals("Score must remain 10 on duplicate solve", 10, state.totalScore)

        // 2. Record Coding level completion
        ComputerProgressManager.recordCodingLevelCompletion(context, 1, starsEarned = 2)
        state = ComputerProgressManager.state.value
        assertTrue("Level 1 must be completed", state.completedCodingLevels.contains(1))
        assertEquals("Total stars must be 1 + 2 = 3", 3, state.totalStars)
        assertEquals("Total score must be 10 + 20 = 30", 30, state.totalScore)

        // 3. Record Component explored
        ComputerProgressManager.recordComponentExplored(context, "cpu")
        state = ComputerProgressManager.state.value
        assertTrue("CPU must be explored", state.exploredComponents.contains("cpu"))
        assertEquals("Total stars must be 3 + 1 = 4", 4, state.totalStars)
        assertEquals("Total score must be 30 + 5 = 35", 35, state.totalScore)

        // 4. Record Safety badge earned
        ComputerProgressManager.recordSafetyBadgeEarned(context, "badge_password_hero")
        state = ComputerProgressManager.state.value
        assertTrue("Safety badge must be recorded", state.earnedSafetyBadges.contains("badge_password_hero"))
        assertEquals("Total stars must be 4 + 2 = 6", 6, state.totalStars)
        assertEquals("Total score must be 35 + 20 = 55", 55, state.totalScore)

        // 5. Reset progress
        ComputerProgressManager.resetProgress(context)
        state = ComputerProgressManager.state.value
        assertEquals("Stars reset to 0", 0, state.totalStars)
        assertEquals("Score reset to 0", 0, state.totalScore)
        assertTrue("Quizzes reset to empty", state.solvedQuizIds.isEmpty())
        assertTrue("Levels reset to empty", state.completedCodingLevels.isEmpty())
        assertTrue("Badges reset to empty", state.earnedSafetyBadges.isEmpty())
        assertTrue("Components reset to empty", state.exploredComponents.isEmpty())
    }
}
