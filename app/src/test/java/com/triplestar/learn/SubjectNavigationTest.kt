package com.triplestar.learn

import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.repository.CurriculumRepository
import com.triplestar.learn.navigation.NavRoutes
import com.triplestar.learn.util.BadgeManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SubjectNavigationTest {

    private val curriculumRepo = CurriculumRepository()

    @Test
    fun `verify subject detail routes for all curriculum subjects`() {
        val testChildId = "test_child_456"
        val expectedSubjects = listOf("english", "math", "science", "art", "computer", "urdu", "general_knowledge")

        expectedSubjects.forEach { subjectId ->
            val route = NavRoutes.SubjectDetail.createRoute(testChildId, subjectId)
            assertEquals("subject_detail/$testChildId/$subjectId", route)
            assertTrue("Route must contain childId", route.contains(testChildId))
            assertTrue("Route must contain subjectId", route.contains(subjectId))
        }
    }

    @Test
    fun `verify all subjects are provided by curriculum repository`() = runBlocking {
        val subjects = curriculumRepo.getSubjects().first()
        val subjectIds = subjects.map { it.id }

        assertEquals(7, subjects.size)
        assertTrue(subjectIds.contains("english"))
        assertTrue(subjectIds.contains("math"))
        assertTrue(subjectIds.contains("science"))
        assertTrue(subjectIds.contains("art"))
        assertTrue(subjectIds.contains("computer"))
        assertTrue(subjectIds.contains("urdu"))
        assertTrue(subjectIds.contains("general_knowledge"))

        // Verify English and Urdu titles exist for all 7
        subjects.forEach { subject ->
            assertTrue(subject.titleEn.isNotBlank())
            assertTrue(subject.titleUr.isNotBlank())
        }
    }

    @Test
    fun `verify lessons and steps exist for each subject`() {
        val subjectIds = listOf("english", "math", "science", "art", "computer", "urdu", "general_knowledge")

        subjectIds.forEach { subjectId ->
            val lessons = curriculumRepo.getLessonsForSubject(subjectId)
            assertTrue("Subject $subjectId must have lessons", lessons.isNotEmpty())

            lessons.forEach { lesson ->
                assertEquals(subjectId, lesson.subjectId)
                val steps = curriculumRepo.getStepsForLesson(lesson.id)
                assertTrue("Lesson ${lesson.id} must have steps", steps.isNotEmpty())
            }
        }
    }

    @Test
    fun `verify badge manager unlocks math and english stickers`() {
        val mathProgress = ChildProgress(
            id = "child_m",
            childId = "child_1",
            parentId = "parent_1",
            subjectId = "math",
            completedStepIds = listOf("m_step_1", "m_step_2"),
            totalStars = 3
        )
        val englishProgress = ChildProgress(
            id = "child_e",
            childId = "child_1",
            parentId = "parent_1",
            subjectId = "english",
            completedStepIds = listOf("e_step_1"),
            totalStars = 1
        )

        val statuses = BadgeManager.evaluateBadges(
            mathProgress = mathProgress,
            englishProgress = englishProgress
        )

        val mathExplorer = statuses.first { it.badge.id == "math_explorer" }
        assertTrue("Math Explorer should be unlocked", mathExplorer.isUnlocked)

        val mathWhiz = statuses.first { it.badge.id == "math_whiz" }
        assertTrue("Math Whiz should be unlocked", mathWhiz.isUnlocked)

        val mathStarChamp = statuses.first { it.badge.id == "math_star_champ" }
        assertTrue("Math Star Champ should be unlocked with 3 stars", mathStarChamp.isUnlocked)

        val englishPhonics = statuses.first { it.badge.id == "english_phonics" }
        assertTrue("English Phonics should be unlocked", englishPhonics.isUnlocked)

        val dualScholar = statuses.first { it.badge.id == "dual_scholar" }
        assertTrue("Dual Scholar should be unlocked for progress in both", dualScholar.isUnlocked)
    }

    @Test
    fun `verify checkForNewUnlock identifies badge on completion`() {
        val newBadge = BadgeManager.checkForNewUnlock(
            subjectId = "math",
            previousCompletedCount = 0,
            newCompletedCount = 1,
            previousStars = 0,
            newStars = 1
        )
        assertNotNull(newBadge)
        assertEquals("math_explorer", newBadge?.id)
    }

    @Test
    fun `verify educational categories icons and color attributes are configured`() = runBlocking {
        val subjects = curriculumRepo.getSubjects().first()
        val subjectMap = subjects.associateBy { it.id }

        val requiredCategories = listOf("english", "math", "science", "art", "computer")
        requiredCategories.forEach { categoryId ->
            val category = subjectMap[categoryId]
            assertNotNull("Category $categoryId must exist", category)
            assertTrue("Category $categoryId must have non-blank title", category!!.titleEn.isNotBlank())
            assertTrue("Category $categoryId must have non-blank subtitle", category.subtitleEn.isNotBlank())
            assertTrue("Category $categoryId must have valid iconName", category.iconName.isNotBlank())
            assertTrue("Category $categoryId must have colorHex configured", category.colorHex != 0L)
        }
    }
}
