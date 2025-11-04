package com.example.taskreminder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskreminder.data.entity.Subject
import com.example.taskreminder.data.entity.Task
import com.example.taskreminder.ui.components.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    subjects: List<Subject>,
    selectedSubjectId: Long?,
    onTaskClick: (Long) -> Unit,
    onAddTaskClick: () -> Unit,
    onSubjectFilterClick: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedSubjectId) {
                            null -> "All Tasks"
                            -1L -> "Tasks Without Subject"
                            else -> subjects.find { it.id == selectedSubjectId }?.name ?: "Tasks"
                        }
                    )
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter by subject")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tasks yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    val subjectName = task.subjectId?.let { id ->
                        subjects.find { it.id == id }?.name
                    }
                    TaskCard(
                        task = task,
                        subjectName = subjectName,
                        onClick = { onTaskClick(task.id) }
                    )
                }
            }
        }

        if (showFilterDialog) {
            FilterDialog(
                subjects = subjects,
                selectedSubjectId = selectedSubjectId,
                onDismiss = { showFilterDialog = false },
                onSubjectSelected = { subjectId ->
                    onSubjectFilterClick(subjectId)
                    showFilterDialog = false
                }
            )
        }
    }
}

@Composable
fun FilterDialog(
    subjects: List<Subject>,
    selectedSubjectId: Long?,
    onDismiss: () -> Unit,
    onSubjectSelected: (Long?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter by Subject") },
        text = {
            Column {
                // All tasks option
                FilterOption(
                    text = "All Tasks",
                    isSelected = selectedSubjectId == null,
                    onClick = { onSubjectSelected(null) }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Tasks without subject
                FilterOption(
                    text = "Tasks Without Subject",
                    isSelected = selectedSubjectId == -1L,
                    onClick = { onSubjectSelected(-1L) }
                )

                if (subjects.isNotEmpty()) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    subjects.forEach { subject ->
                        FilterOption(
                            text = subject.name,
                            isSelected = selectedSubjectId == subject.id,
                            onClick = { onSubjectSelected(subject.id) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
