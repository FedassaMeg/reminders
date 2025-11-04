package com.example.taskreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.taskreminder.navigation.TaskReminderApp
import com.example.taskreminder.ui.theme.TaskReminderTheme
import com.example.taskreminder.ui.viewmodel.SubjectViewModel
import com.example.taskreminder.ui.viewmodel.SubjectViewModelFactory
import com.example.taskreminder.ui.viewmodel.TaskViewModel
import com.example.taskreminder.ui.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TaskReminderApplication).taskRepository)
    }

    private val subjectViewModel: SubjectViewModel by viewModels {
        SubjectViewModelFactory((application as TaskReminderApplication).subjectRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskReminderApp(
                        taskViewModel = taskViewModel,
                        subjectViewModel = subjectViewModel
                    )
                }
            }
        }
    }
}
