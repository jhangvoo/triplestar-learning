package com.triplestar.learn

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.Timestamp
import com.triplestar.learn.base.FirestoreEmulatorTestBase
import com.triplestar.learn.data.model.ChildProfile
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepType
import com.triplestar.learn.data.repository.ChildProfileRepository
import com.triplestar.learn.data.repository.ProgressRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TripleStarRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun authenticatedParentCanCreateAndRetrieveChildProfile() = runBlocking {
        val parentUid = signInTestUser("parent_alice_${System.currentTimeMillis()}@example.com")
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = ChildProfileRepository(context)
        val childId = "child_test_${System.currentTimeMillis()}"

        val child = ChildProfile(
            id = childId,
            parentId = parentUid,
            displayName = "Amina",
            avatarId = "avatar_star",
            age = 8,
            learningLevel = "INTERMEDIATE",
            dailyTargetMinutes = 20,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )

        val createResult = repo.createChildProfile(child)
        assertTrue("Child profile creation should succeed", createResult.isSuccess)

        val children = repo.observeChildren(parentUid).first()
        val retrieved = children.find { it.id == childId }
        assertNotNull("Retrieved child profile should not be null", retrieved)
        assertEquals("Amina", retrieved?.displayName)
        assertEquals(8, retrieved?.age)
    }

    @Test
    fun crossParentAccessIsPrevented() = runBlocking {
        // Alice creates a profile
        val aliceUid = signInTestUser("parent_alice_cross_${System.currentTimeMillis()}@example.com")
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = ChildProfileRepository(context)
        val childId = "alice_secret_${System.currentTimeMillis()}"

        val child = ChildProfile(
            id = childId,
            parentId = aliceUid,
            displayName = "Zayn",
            avatarId = "avatar_star",
            age = 6,
            learningLevel = "BEGINNER",
            dailyTargetMinutes = 15,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )
        repo.createChildProfile(child)

        // Switch user session to Bob
        signInTestUser("parent_bob_cross_${System.currentTimeMillis()}@example.com")

        // Bob attempts to observe Alice's children
        val bobObservedAliceChildren = repo.observeChildren(aliceUid).first()
        assertTrue("Bob should not receive any of Alice's children", bobObservedAliceChildren.isEmpty())
    }

    @Test
    fun parentCanRecordAndObserveStepCompletion() = runBlocking {
        val parentUid = signInTestUser("parent_carol_${System.currentTimeMillis()}@example.com")
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val progressRepo = ProgressRepository(context)
        val childId = "child_carol_${System.currentTimeMillis()}"

        val step1 = Step(
            id = "eng_step_1",
            lessonId = "eng_lesson_1",
            stepNumber = 1,
            stepType = StepType.LEARN.name,
            xpReward = 15,
            starsReward = 1
        )
        val allSteps = listOf(step1)

        val recordResult = progressRepo.recordStepCompletion(
            childId = childId,
            parentId = parentUid,
            subjectId = "english",
            step = step1,
            allStepsInLesson = allSteps
        )
        assertTrue(
            "Recording step completion should succeed but failed with: ${recordResult.exceptionOrNull()?.message}",
            recordResult.isSuccess
        )

        val observedProgress = progressRepo.observeProgress(childId, "english").first()
        assertNotNull("Observed progress should not be null", observedProgress)
        assertEquals(15, observedProgress?.totalXp)
        assertEquals(1, observedProgress?.totalStars)
        assertTrue(observedProgress?.completedStepIds?.contains("eng_step_1") == true)
    }

    @Test
    fun ageGroupsAndAvatarSelectionArePersistedCorrectly() = runBlocking {
        val parentUid = signInTestUser("parent_age_test_${System.currentTimeMillis()}@example.com")
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val repo = ChildProfileRepository(context)

        // Test age groups: 5-7 (Explorer), 8-10 (Adventurer), 11-14 (Master)
        val childProfiles = listOf(
            ChildProfile(id = "c5", parentId = parentUid, displayName = "Kid5", age = 6, learningLevel = "BEGINNER", avatarId = "avatar_star"),
            ChildProfile(id = "c8", parentId = parentUid, displayName = "Kid8", age = 9, learningLevel = "INTERMEDIATE", avatarId = "avatar_explorer"),
            ChildProfile(id = "c11", parentId = parentUid, displayName = "Kid11", age = 12, learningLevel = "ADVANCED", avatarId = "avatar_hero")
        )

        for (child in childProfiles) {
            val res = repo.createChildProfile(child)
            assertTrue("Creating profile ${child.id} should succeed", res.isSuccess)
        }

        val observedChildren = repo.observeChildren(parentUid).first()
        assertEquals(3, observedChildren.size)
        val c5Retrieved = observedChildren.find { it.id == "c5" }
        assertNotNull(c5Retrieved)
        assertEquals(6, c5Retrieved?.age)
        assertEquals("BEGINNER", c5Retrieved?.learningLevel)
        assertEquals("avatar_star", c5Retrieved?.avatarId)
    }
}
