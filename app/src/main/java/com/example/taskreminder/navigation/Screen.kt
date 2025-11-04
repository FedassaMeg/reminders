package com.example.taskreminder.navigation

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Long? = null) = if (taskId == null) "task_detail/new" else "task_detail/$taskId"
    }
    object SubjectList : Screen("subject_list")
    object SubjectDetail : Screen("subject_detail/{subjectId}") {
        fun createRoute(subjectId: Long? = null) = if (subjectId == null) "subject_detail/new" else "subject_detail/$subjectId"
    }
}
