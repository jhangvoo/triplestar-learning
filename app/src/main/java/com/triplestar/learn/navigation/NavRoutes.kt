package com.triplestar.learn.navigation

sealed class NavRoutes(val route: String) {
    data object Splash : NavRoutes("splash")
    data object Auth : NavRoutes("auth")
    data object ChildSelect : NavRoutes("child_select")
    data object CreateChildProfile : NavRoutes("create_child_profile")
    data object MainDashboard : NavRoutes("main_dashboard/{childId}") {
        fun createRoute(childId: String): String = "main_dashboard/$childId"
    }
    data object ParentSettings : NavRoutes("parent_settings")
    data object SubjectDetail : NavRoutes("subject_detail/{childId}/{subjectId}") {
        fun createRoute(childId: String, subjectId: String): String = "subject_detail/$childId/$subjectId"
    }
    data object StepActivity : NavRoutes("step_activity/{childId}/{subjectId}/{lessonId}/{stepId}") {
        fun createRoute(childId: String, subjectId: String, lessonId: String, stepId: String): String =
            "step_activity/$childId/$subjectId/$lessonId/$stepId"
    }
}
