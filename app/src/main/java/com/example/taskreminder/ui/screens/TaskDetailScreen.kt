package com.example.taskreminder.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskreminder.data.entity.Subject
import com.example.taskreminder.data.entity.Task
import com.example.taskreminder.data.model.TaskPriority
import com.example.taskreminder.data.model.TaskStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task?,
    isNewTask: Boolean,
    subjects: List<Subject>,
    onSave: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var selectedSubjectId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedPriority by rememberSaveable { mutableStateOf(TaskPriority.MEDIUM) }
    var selectedStatus by rememberSaveable { mutableStateOf(TaskStatus.TODO) }
    var deadline by rememberSaveable { mutableStateOf<Long?>(null) }

    LaunchedEffect(task?.id, isNewTask) {
        if (!isNewTask && task != null) {
            title = task.title
            description = task.description
            notes = task.notes
            selectedSubjectId = task.subjectId
            selectedPriority = task.priority
            selectedStatus = task.status
            deadline = task.deadline
        }

        if (isNewTask && task == null) {
            title = ""
            description = ""
            notes = ""
            selectedSubjectId = null
            selectedPriority = TaskPriority.MEDIUM
            selectedStatus = TaskStatus.TODO
            deadline = null
        }
    }

    var showSubjectDialog by remember { mutableStateOf(false) }
    var showPriorityDialog by remember { mutableStateOf(false) }
    var showStatusDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewTask) "New Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isNewTask && task != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Subject selector
            OutlinedCard(
                onClick = { showSubjectDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subject")
                    Text(
                        text = selectedSubjectId?.let { id ->
                            subjects.find { it.id == id }?.name
                        } ?: "None",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Priority selector
            OutlinedCard(
                onClick = { showPriorityDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Priority")
                    Text(
                        text = selectedPriority.name,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Status selector
            OutlinedCard(
                onClick = { showStatusDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Status")
                    Text(
                        text = selectedStatus.name.replace("_", " "),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Deadline display (simplified - in real app would use date picker)
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Deadline")
                    Text(
                        text = deadline?.let {
                            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Not set",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newTask = Task(
                            id = task?.id ?: 0,
                            title = title,
                            description = description,
                            notes = notes,
                            subjectId = selectedSubjectId,
                            priority = selectedPriority,
                            status = selectedStatus,
                            deadline = deadline,
                            createdAt = task?.createdAt ?: System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis(),
                            completedAt = if (selectedStatus == TaskStatus.COMPLETED)
                                System.currentTimeMillis() else null
                        )
                        onSave(newTask)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text("Save")
            }
        }

        // Dialogs
        if (showSubjectDialog) {
            SelectionDialog(
                title = "Select Subject",
                options = listOf(null) + subjects.map { it.id },
                selectedOption = selectedSubjectId,
                optionLabel = { id ->
                    id?.let { subjects.find { it.id == id }?.name } ?: "None"
                },
                onDismiss = { showSubjectDialog = false },
                onSelect = {
                    selectedSubjectId = it
                    showSubjectDialog = false
                }
            )
        }

        if (showPriorityDialog) {
            SelectionDialog(
                title = "Select Priority",
                options = TaskPriority.entries,
                selectedOption = selectedPriority,
                optionLabel = { it.name },
                onDismiss = { showPriorityDialog = false },
                onSelect = {
                    selectedPriority = it
                    showPriorityDialog = false
                }
            )
        }

        if (showStatusDialog) {
            SelectionDialog(
                title = "Select Status",
                options = TaskStatus.entries,
                selectedOption = selectedStatus,
                optionLabel = { it.name.replace("_", " ") },
                onDismiss = { showStatusDialog = false },
                onSelect = {
                    selectedStatus = it
                    showStatusDialog = false
                }
            )
        }

        if (showDeleteDialog && task != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Task") },
                text = { Text("Are you sure you want to delete this task?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete(task)
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun <T> SelectionDialog(
    title: String,
    options: List<T>,
    selectedOption: T?,
    optionLabel: (T) -> String,
    onDismiss: () -> Unit,
    onSelect: (T) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    FilterOption(
                        text = optionLabel(option),
                        isSelected = option == selectedOption,
                        onClick = { onSelect(option) }
                    )
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
