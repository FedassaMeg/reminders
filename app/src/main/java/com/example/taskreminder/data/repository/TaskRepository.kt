package com.example.taskreminder.data.repository

import com.example.taskreminder.data.dao.TaskDao
import com.example.taskreminder.data.entity.Task
import com.example.taskreminder.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(id: Long): Flow<Task?> = taskDao.getTaskById(id)

    fun getTasksBySubject(subjectId: Long): Flow<List<Task>> =
        taskDao.getTasksBySubject(subjectId)

    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>> =
        taskDao.getTasksByStatus(status)

    fun getTasksWithoutSubject(): Flow<List<Task>> =
        taskDao.getTasksWithoutSubject()

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun deleteTaskById(id: Long) = taskDao.deleteTaskById(id)

    suspend fun updateTaskStatus(id: Long, status: TaskStatus) =
        taskDao.updateTaskStatus(id, status)
}
