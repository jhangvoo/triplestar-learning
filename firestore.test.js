const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ALICE_UID = "parent_alice";
const BOB_UID = "parent_bob";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

// 1. Unauthenticated access
test("Unauthenticated user: cannot read or write parent profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("parents").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("parents").doc(ALICE_UID).set({ id: ALICE_UID }));
});

test("Unauthenticated user: cannot read child profiles", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("parents").doc(ALICE_UID).collection("children").get());
});

// 2. Cross-parent isolation
test("Cross-parent isolation: Alice cannot read or write Bob's profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("parents").doc(BOB_UID).get());
  await assertFails(aliceDb.collection("parents").doc(BOB_UID).set({ id: BOB_UID, email: "bob@example.com" }));
});

test("Cross-parent isolation: Alice cannot access Bob's children", async () => {
  // Seed Bob's child as admin
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore()
      .collection("parents")
      .doc(BOB_UID)
      .collection("children")
      .doc("bob_child_1")
      .set({
        id: "bob_child_1",
        parentId: BOB_UID,
        displayName: "Billy",
        avatarId: "avatar_star",
        age: 8,
        learningLevel: "INTERMEDIATE",
        dailyTargetMinutes: 20,
        createdAt: new Date(),
        updatedAt: new Date()
      });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  // Alice cannot read Bob's child
  await assertFails(
    aliceDb.collection("parents").doc(BOB_UID).collection("children").doc("bob_child_1").get()
  );
  // Alice cannot write into Bob's children
  await assertFails(
    aliceDb.collection("parents").doc(BOB_UID).collection("children").doc("alice_hacked").set({
      id: "alice_hacked",
      parentId: BOB_UID,
      displayName: "Intruder"
    })
  );
});

// 3. Authenticated parent managing their own children
test("Parent ownership: Alice can create and read her own child profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  await assertSucceeds(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_1").set({
      id: "alice_child_1",
      parentId: ALICE_UID,
      displayName: "Sara",
      avatarId: "avatar_star",
      age: 7,
      learningLevel: "BEGINNER",
      dailyTargetMinutes: 15,
      createdAt: now,
      updatedAt: now
    })
  );

  await assertSucceeds(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_1").get()
  );
});

test("Parent ownership: Alice can update and delete her own child profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  // Create child
  await assertSucceeds(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_2").set({
      id: "alice_child_2",
      parentId: ALICE_UID,
      displayName: "Zayn",
      avatarId: "avatar_star",
      age: 6,
      learningLevel: "BEGINNER",
      dailyTargetMinutes: 15,
      createdAt: now,
      updatedAt: now
    })
  );

  // Update child (valid)
  await assertSucceeds(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_2").update({
      displayName: "Zayn Updated",
      age: 7,
      updatedAt: new Date()
    })
  );

  // Reassign parentId (invalid - must fail)
  await assertFails(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_2").update({
      parentId: BOB_UID
    })
  );

  // Delete child (valid)
  await assertSucceeds(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_2").delete()
  );
});

test("Parent ownership: Alice cannot create a child with mismatched parentId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  await assertFails(
    aliceDb.collection("parents").doc(ALICE_UID).collection("children").doc("alice_child_spoofed").set({
      id: "alice_child_spoofed",
      parentId: BOB_UID, // Mismatched parentId
      displayName: "Spoofed",
      avatarId: "avatar_star",
      age: 8,
      learningLevel: "INTERMEDIATE",
      dailyTargetMinutes: 20,
      createdAt: now,
      updatedAt: now
    })
  );
});

// 4. Curriculum read-only protection
test("Curriculum: Authenticated users can read subjects, but cannot modify them", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("subjects").doc("math").set({
      id: "math",
      titleEn: "Mathematics",
      titleUr: "ریاضی",
      displayOrder: 1,
      isPublished: true
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  // Read succeeds
  await assertSucceeds(aliceDb.collection("subjects").doc("math").get());
  // Write fails
  await assertFails(
    aliceDb.collection("subjects").doc("math").update({ titleEn: "Hacked Math" })
  );
});

// 5. Progress isolation
test("Progress: Alice cannot modify progress for Bob's child", async () => {
  const now = new Date();
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();

  await assertFails(
    aliceDb.collection("progress").doc("bob_child_1_math").set({
      id: "bob_child_1_math",
      childId: "bob_child_1",
      parentId: BOB_UID, // Not Alice
      subjectId: "math",
      completedStepIds: ["s1"],
      unlockedStepIds: ["s1", "s2"],
      totalXp: 100,
      totalStars: 5,
      streakDays: 1,
      updatedAt: now
    })
  );
});

test("Progress: Alice can create and update progress for her own child", async () => {
  const now = new Date();
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();

  await assertSucceeds(
    aliceDb.collection("progress").doc("alice_child_1_math").set({
      id: "alice_child_1_math",
      childId: "alice_child_1",
      parentId: ALICE_UID,
      subjectId: "math",
      completedStepIds: ["s1"],
      unlockedStepIds: ["s1", "s2"],
      totalXp: 15,
      totalStars: 1,
      streakDays: 1,
      updatedAt: now
    })
  );

  await assertSucceeds(
    aliceDb.collection("progress").doc("alice_child_1_math").get()
  );
});
