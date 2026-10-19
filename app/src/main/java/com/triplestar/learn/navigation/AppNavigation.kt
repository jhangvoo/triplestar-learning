package com.triplestar.learn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.triplestar.learn.data.repository.AdRepository
import com.triplestar.learn.data.repository.AuthRepository
import com.triplestar.learn.data.repository.ChildProfileRepository
import com.triplestar.learn.data.repository.CurriculumRepository
import com.triplestar.learn.data.repository.ProgressRepository
import com.triplestar.learn.data.state.AuthState
import com.triplestar.learn.ui.screens.AuthScreen
import com.triplestar.learn.ui.screens.ChildSelectionScreen
import com.triplestar.learn.ui.screens.CreateChildProfileScreen
import com.triplestar.learn.ui.screens.MainDashboardScreen
import com.triplestar.learn.ui.screens.ParentSettingsScreen
import com.triplestar.learn.ui.screens.SplashScreen
import com.triplestar.learn.ui.screens.StepActivityScreen
import com.triplestar.learn.ui.screens.SubjectDetailScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val childRepository = remember { ChildProfileRepository(context) }
    val curriculumRepository = remember { CurriculumRepository() }
    val progressRepository = remember { ProgressRepository(context) }
    val adRepository = remember { AdRepository() }

    val authState by authRepository.authState.collectAsState()
    val currentParentId = when (val state = authState) {
        is AuthState.Authenticated -> state.userId
        else -> try {
            com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    val children by childRepository.observeChildren(currentParentId).collectAsState(initial = emptyList())
    val subjects by curriculumRepository.getSubjects().collectAsState(initial = emptyList())

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
        modifier = modifier
    ) {
        // Splash Destination
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val destination = if (authState is AuthState.Authenticated || try {
                        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null
                    } catch (e: Exception) {
                        false
                    }) {
                        NavRoutes.ChildSelect.route
                    } else {
                        NavRoutes.Auth.route
                    }
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Parent Authentication Destination
        composable(NavRoutes.Auth.route) {
            AuthScreen(
                authRepository = authRepository,
                authState = authState,
                onNavigateToChildSelect = {
                    navController.navigate(NavRoutes.ChildSelect.route) {
                        popUpTo(NavRoutes.Auth.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Child Profile Selection
        composable(NavRoutes.ChildSelect.route) {
            ChildSelectionScreen(
                children = children,
                onChildSelected = { childId ->
                    navController.navigate(NavRoutes.MainDashboard.createRoute(childId))
                },
                onNavigateToAddChild = {
                    navController.navigate(NavRoutes.CreateChildProfile.route)
                },
                onNavigateToParentSettings = {
                    navController.navigate(NavRoutes.ParentSettings.route)
                }
            )
        }

        // Create Child Profile
        composable(NavRoutes.CreateChildProfile.route) {
            CreateChildProfileScreen(
                parentId = currentParentId,
                repository = childRepository,
                onProfileCreated = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Child Learning Dashboard
        composable(
            route = NavRoutes.MainDashboard.route,
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId") ?: ""
            val activeChild = children.firstOrNull { it.id == childId }
                ?: children.firstOrNull()
                ?: com.triplestar.learn.data.model.ChildProfile(
                    id = childId,
                    displayName = "Learner",
                    age = 7
                )

            val childProgress by progressRepository.observeProgress(
                childId = activeChild.id,
                subjectId = "english"
            ).collectAsState(initial = null)

            MainDashboardScreen(
                child = activeChild,
                progress = childProgress,
                subjects = subjects,
                adRepository = adRepository,
                onSwitchLearner = {
                    navController.navigate(NavRoutes.ChildSelect.route) {
                        popUpTo(NavRoutes.MainDashboard.route) { inclusive = true }
                    }
                },
                onNavigateToParentSettings = {
                    navController.navigate(NavRoutes.ParentSettings.route)
                }
            )
        }

        // Parent Settings Screen
        composable(NavRoutes.ParentSettings.route) {
            ParentSettingsScreen(
                parentId = currentParentId,
                children = children,
                childRepository = childRepository,
                authRepository = authRepository,
                onBack = { navController.popBackStack() },
                onSignedOut = {
                    navController.navigate(NavRoutes.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Subject Detail (Curriculum & Lessons)
        composable(
            route = NavRoutes.SubjectDetail.route,
            arguments = listOf(
                navArgument("childId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId") ?: ""
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""

            SubjectDetailScreen(
                childId = childId,
                subjectId = subjectId,
                curriculumRepository = curriculumRepository,
                progressRepository = progressRepository,
                adRepository = adRepository,
                onStepClick = { lessonId, stepId ->
                    navController.navigate(NavRoutes.StepActivity.createRoute(childId, subjectId, lessonId, stepId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Step Activity (Interactive Learning & Step Completion)
        composable(
            route = NavRoutes.StepActivity.route,
            arguments = listOf(
                navArgument("childId") { type = NavType.StringType },
                navArgument("subjectId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType },
                navArgument("stepId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId") ?: ""
            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            val stepId = backStackEntry.arguments?.getString("stepId") ?: ""

            StepActivityScreen(
                childId = childId,
                parentId = currentParentId,
                subjectId = subjectId,
                lessonId = lessonId,
                stepId = stepId,
                curriculumRepository = curriculumRepository,
                progressRepository = progressRepository,
                onStepCompleted = {
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
