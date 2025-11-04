package com.example.taskreminder.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.taskreminder.data.entity.Task
import com.example.taskreminder.data.model.TaskPriority
import com.example.taskreminder.data.model.TaskStatus
import com.example.taskreminder.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    subjectName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, getPriorityColor(task.priority).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (task.status == TaskStatus.COMPLETED)
                            TextDecoration.LineThrough else null,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (task.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Icon(
                    imageVector = if (task.status == TaskStatus.COMPLETED)
                        Icons.Default.CheckCircle else Icons.Default.Circle,
                    contentDescription = "Status",
                    tint = getStatusColor(task.status),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority badge
                PriorityBadge(priority = task.priority)

                // Status badge
                StatusBadge(status = task.status)

                // Subject badge
                if (subjectName != null) {
                    SubjectBadge(name = subjectName)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Deadline
                task.deadline?.let { deadline ->
                    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                    Text(
                        text = dateFormat.format(Date(deadline)),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (deadline < System.currentTimeMillis() && task.status != TaskStatus.COMPLETED)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun PriorityBadge(priority: TaskPriority) {
    Surface(
        color = getPriorityColor(priority).copy(alpha = 0.2f),
        contentColor = getPriorityColor(priority),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = priority.name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StatusBadge(status: TaskStatus) {
    Surface(
        color = getStatusColor(status).copy(alpha = 0.2f),
        contentColor = getStatusColor(status),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name.replace("_", " "),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SubjectBadge(name: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun getPriorityColor(priority: TaskPriority) = when (priority) {
    TaskPriority.LOW -> PriorityLow
    TaskPriority.MEDIUM -> PriorityMedium
    TaskPriority.HIGH -> PriorityHigh
    TaskPriority.URGENT -> PriorityUrgent
}

@Composable
fun getStatusColor(status: TaskStatus) = when (status) {
    TaskStatus.TODO -> StatusTodo
    TaskStatus.IN_PROGRESS -> StatusInProgress
    TaskStatus.COMPLETED -> StatusCompleted
    TaskStatus.CANCELLED -> StatusCancelled
}
