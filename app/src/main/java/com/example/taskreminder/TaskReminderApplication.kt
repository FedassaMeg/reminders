package com.example.taskreminder

import android.app.Application
import com.example.taskreminder.data.TaskReminderDatabase
import com.example.taskreminder.data.repository.SubjectRepository
import com.example.taskreminder.data.repository.TaskRepository

class TaskReminderApplication : Application() {
    private val database by lazy { TaskReminderDatabase.getDatabase(this) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }
    val subjectRepository by lazy { SubjectRepository(database.subjectDao()) }
}
