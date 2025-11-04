package com.example.taskreminder.data.dao

import androidx.room.*
import com.example.taskreminder.data.entity.Task
import com.example.taskreminder.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY deadline ASC, priority DESC, createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<Task?>

    @Query("SELECT * FROM tasks WHERE subjectId = :subjectId ORDER BY deadline ASC, priority DESC")
    fun getTasksBySubject(subjectId: Long): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY deadline ASC, priority DESC")
    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE subjectId IS NULL ORDER BY deadline ASC, priority DESC")
    fun getTasksWithoutSubject(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Query("UPDATE tasks SET status = :status, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateTaskStatus(id: Long, status: TaskStatus, timestamp: Long = System.currentTimeMillis())
}
