package com.example.taskreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskreminder.data.dao.SubjectDao
import com.example.taskreminder.data.dao.TaskDao
import com.example.taskreminder.data.entity.Subject
import com.example.taskreminder.data.entity.Task

@Database(
    entities = [Task::class, Subject::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TaskReminderDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subjectDao(): SubjectDao

    companion object {
        @Volatile
        private var INSTANCE: TaskReminderDatabase? = null

        fun getDatabase(context: Context): TaskReminderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskReminderDatabase::class.java,
                    "task_reminder_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
