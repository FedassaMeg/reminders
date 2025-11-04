package com.example.taskreminder.data.repository

import com.example.taskreminder.data.dao.SubjectDao
import com.example.taskreminder.data.entity.Subject
import kotlinx.coroutines.flow.Flow

class SubjectRepository(private val subjectDao: SubjectDao) {
    fun getAllSubjects(): Flow<List<Subject>> = subjectDao.getAllSubjects()

    fun getSubjectById(id: Long): Flow<Subject?> = subjectDao.getSubjectById(id)

    suspend fun insertSubject(subject: Subject): Long = subjectDao.insertSubject(subject)

    suspend fun updateSubject(subject: Subject) = subjectDao.updateSubject(subject)

    suspend fun deleteSubject(subject: Subject) = subjectDao.deleteSubject(subject)

    suspend fun deleteSubjectById(id: Long) = subjectDao.deleteSubjectById(id)
}
