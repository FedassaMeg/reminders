package com.example.taskreminder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskreminder.data.entity.Task
import com.example.taskreminder.data.model.TaskStatus
import com.example.taskreminder.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    val allTasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedSubjectId = MutableStateFlow<Long?>(null)
    val selectedSubjectId: StateFlow<Long?> = _selectedSubjectId.asStateFlow()

    private val _filteredTasks = MutableStateFlow<List<Task>>(emptyList())
    val filteredTasks: StateFlow<List<Task>> = _filteredTasks.asStateFlow()

    init {
        // Observe all tasks and apply filtering
        viewModelScope.launch {
            allTasks.collect { tasks ->
                applyFilter(tasks)
            }
        }
    }

    fun selectSubject(subjectId: Long?) {
        _selectedSubjectId.value = subjectId
        applyFilter(allTasks.value)
    }

    private fun applyFilter(tasks: List<Task>) {
        _filteredTasks.value = when (val subjectId = _selectedSubjectId.value) {
            null -> tasks
            -1L -> tasks.filter { it.subjectId == null } // Tasks without subject
            else -> tasks.filter { it.subjectId == subjectId }
        }
    }

    fun getTasksBySubject(subjectId: Long): StateFlow<List<Task>> {
        return repository.getTasksBySubject(subjectId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getTaskById(id: Long): StateFlow<Task?> {
        return repository.getTaskById(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateTaskStatus(taskId: Long, status: TaskStatus) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, status)
        }
    }
}
