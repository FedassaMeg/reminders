package com.example.taskreminder.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskreminder.ui.screens.*
import com.example.taskreminder.ui.viewmodel.SubjectViewModel
import com.example.taskreminder.ui.viewmodel.TaskViewModel

@Composable
fun TaskReminderApp(
    taskViewModel: TaskViewModel,
    subjectViewModel: SubjectViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.TaskList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Task List Screen
            composable(Screen.TaskList.route) {
                val tasks by taskViewModel.filteredTasks.collectAsState()
                val subjects by subjectViewModel.allSubjects.collectAsState()
                val selectedSubjectId by taskViewModel.selectedSubjectId.collectAsState()

                TaskListScreen(
                    tasks = tasks,
                    subjects = subjects,
                    selectedSubjectId = selectedSubjectId,
                    onTaskClick = { taskId ->
                        navController.navigate(Screen.TaskDetail.createRoute(taskId))
                    },
                    onAddTaskClick = {
                        navController.navigate(Screen.TaskDetail.createRoute(null))
                    },
                    onSubjectFilterClick = { subjectId ->
                        taskViewModel.selectSubject(subjectId)
                    }
                )
            }

            // Task Detail Screen
            composable(
                route = Screen.TaskDetail.route,
                arguments = listOf(navArgument("taskId") { type = NavType.StringType })
            ) { backStackEntry ->
                val taskIdString = backStackEntry.arguments?.getString("taskId")
                val taskId = if (taskIdString == "new") null else taskIdString?.toLongOrNull()

                val task by if (taskId != null) {
                    taskViewModel.getTaskById(taskId).collectAsState()
                } else {
                    remember { mutableStateOf(null) }
                }
                val subjects by subjectViewModel.allSubjects.collectAsState()

                TaskDetailScreen(
                    task = task,
                    isNewTask = taskId == null,
                    subjects = subjects,
                    onSave = { newTask ->
                        if (taskId == null) {
                            taskViewModel.insertTask(newTask)
                        } else {
                            taskViewModel.updateTask(newTask)
                        }
                        navController.popBackStack()
                    },
                    onDelete = { taskToDelete ->
                        taskViewModel.deleteTask(taskToDelete)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // Subject List Screen
            composable(Screen.SubjectList.route) {
                val subjects by subjectViewModel.allSubjects.collectAsState()

                SubjectListScreen(
                    subjects = subjects,
                    onSubjectClick = { subjectId ->
                        navController.navigate(Screen.SubjectDetail.createRoute(subjectId))
                    },
                    onAddSubjectClick = {
                        navController.navigate(Screen.SubjectDetail.createRoute(null))
                    }
                )
            }

            // Subject Detail Screen
            composable(
                route = Screen.SubjectDetail.route,
                arguments = listOf(navArgument("subjectId") { type = NavType.StringType })
            ) { backStackEntry ->
                val subjectIdString = backStackEntry.arguments?.getString("subjectId")
                val subjectId = if (subjectIdString == "new") null else subjectIdString?.toLongOrNull()

                val subject by if (subjectId != null) {
                    subjectViewModel.getSubjectById(subjectId).collectAsState()
                } else {
                    remember { mutableStateOf(null) }
                }

                SubjectDetailScreen(
                    subject = subject,
                    onSave = { newSubject ->
                        if (subjectId == null) {
                            subjectViewModel.insertSubject(newSubject)
                        } else {
                            subjectViewModel.updateSubject(newSubject)
                        }
                        navController.popBackStack()
                    },
                    onDelete = { subjectToDelete ->
                        subjectViewModel.deleteSubject(subjectToDelete)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Tasks", Icons.Default.Task, Screen.TaskList.route),
        BottomNavItem("Subjects", Icons.Default.List, Screen.SubjectList.route)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
