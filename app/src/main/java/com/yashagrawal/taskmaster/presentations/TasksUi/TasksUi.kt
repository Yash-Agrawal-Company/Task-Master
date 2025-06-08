package com.yashagrawal.taskmaster.presentations.TasksUi

import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import java.util.*

// Data Model
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val priority: TaskPriority = TaskPriority.MEDIUM
)

enum class TaskPriority(val displayName: String, val color: Color) {
    HIGH("High", Color(0xFFEF4444)),
    MEDIUM("Medium", Color(0xFFF59E0B)),
    LOW("Low", Color(0xFF10B981))
}

// Color Theme
val primaryBlue = Color(0xFF1E3A8A)
val lightBlue = Color(0xFF3B82F6)
val backgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E293B),
        Color(0xFF334155)
    )
)

@Composable
fun TaskUi(navController : NavHostController) {
    // State management
    var tasks by remember { mutableStateOf(getFakeTasks()) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            TaskHeader(
                totalTasks = tasks.size,
                completedTasks = tasks.count { it.isCompleted }
            )

            // Task List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { taskId ->
                            tasks = tasks.map {
                                if (it.id == taskId) it.copy(isCompleted = !it.isCompleted)
                                else it
                            }
                        },
                        onDeleteTask = { taskId ->
                            tasks = tasks.filter { it.id != taskId }
                        }
                    )
                }

                // Bottom spacing for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddTaskDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = lightBlue,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Task",
                modifier = Modifier.size(28.dp)
            )
        }

        // Add Task Dialog
        if (showAddTaskDialog) {
            AddTaskDialog(
                taskName = taskName,
                taskDescription = taskDescription,
                selectedPriority = selectedPriority,
                onTaskNameChange = { taskName = it },
                onTaskDescriptionChange = { taskDescription = it },
                onPriorityChange = { selectedPriority = it },
                onDismiss = {
                    showAddTaskDialog = false
                    taskName = ""
                    taskDescription = ""
                    selectedPriority = TaskPriority.MEDIUM
                },
                onSubmit = {
                    if (taskName.isNotBlank()) {
                        val newTask = Task(
                            name = taskName.trim(),
                            description = taskDescription.trim(),
                            priority = selectedPriority
                        )
                        tasks = listOf(newTask) + tasks
                        showAddTaskDialog = false
                        taskName = ""
                        taskDescription = ""
                        selectedPriority = TaskPriority.MEDIUM
                    }
                }
            )
        }
    }
}

@Composable
fun TaskHeader(totalTasks: Int, completedTasks: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Tasks ✅",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Stay organized and productive",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$completedTasks/$totalTasks",
                        color = lightBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Completed",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            // Progress Bar
            Spacer(modifier = Modifier.height(16.dp))
            val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = lightBlue,
                trackColor = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: (String) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                Color.White.copy(alpha = 0.05f)
            else
                Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleComplete(task.id) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = lightBlue,
                        uncheckedColor = Color.White.copy(alpha = 0.6f),
                        checkmarkColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Task Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.name,
                        color = if (task.isCompleted)
                            Color.White.copy(alpha = 0.6f)
                        else
                            Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (task.description.isNotBlank() && isExpanded) {
                        Text(
                            text = task.description,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Priority Badge
                Box(
                    modifier = Modifier
                        .background(
                            color = task.priority.color.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = task.priority.displayName,
                        color = task.priority.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Delete Button
                IconButton(
                    onClick = { onDeleteTask(task.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    taskName: String,
    taskDescription: String,
    selectedPriority: TaskPriority,
    onTaskNameChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    onPriorityChange: (TaskPriority) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E293B)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add New Task",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Task Name Field
                OutlinedTextField(
                    value = taskName,
                    onValueChange = onTaskNameChange,
                    label = { Text("Task Name", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = lightBlue,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        cursorColor = lightBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Task Description Field
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = onTaskDescriptionChange,
                    label = { Text("Task Description", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = lightBlue,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        cursorColor = lightBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Priority Selection
                Text(
                    text = "Priority",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskPriority.values().forEach { priority ->
                        FilterChip(
                            onClick = { onPriorityChange(priority) },
                            label = { Text(priority.displayName) },
                            selected = selectedPriority == priority,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = priority.color.copy(alpha = 0.3f),
                                selectedLabelColor = priority.color,
                                containerColor = Color.White.copy(alpha = 0.1f),
                                labelColor = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White.copy(alpha = 0.7f)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.3f),
                                    Color.White.copy(alpha = 0.3f)
                                )
                            )
                        )
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier.weight(1f),
                        enabled = taskName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightBlue
                        )
                    ) {
                        Text("Add Task", color = Color.White)
                    }
                }
            }
        }
    }
}

// Fake Data
fun getFakeTasks(): List<Task> {
    return listOf(
        Task(
            name = "Complete project proposal",
            description = "Finish the quarterly project proposal and submit it to the team for review. Include budget estimates and timeline.",
            priority = TaskPriority.HIGH,
            isCompleted = false
        ),
        Task(
            name = "Team meeting at 3 PM",
            description = "Weekly team sync to discuss progress and blockers",
            priority = TaskPriority.MEDIUM,
            isCompleted = true
        ),
        Task(
            name = "Review code changes",
            description = "Go through the pull requests and provide feedback to junior developers",
            priority = TaskPriority.MEDIUM,
            isCompleted = false
        ),
        Task(
            name = "Update documentation",
            description = "Update the API documentation with new endpoints and examples",
            priority = TaskPriority.LOW,
            isCompleted = false
        ),
        Task(
            name = "Buy groceries",
            description = "Get milk, bread, eggs, and vegetables for the week",
            priority = TaskPriority.LOW,
            isCompleted = true
        ),
        Task(
            name = "Prepare presentation",
            description = "Create slides for tomorrow's client presentation. Include demo and pricing details.",
            priority = TaskPriority.HIGH,
            isCompleted = false
        ),
        Task(
            name = "Call dentist",
            description = "Schedule appointment for dental cleaning",
            priority = TaskPriority.MEDIUM,
            isCompleted = false
        ),
        Task(
            name = "Exercise workout",
            description = "30 minutes cardio and strength training at the gym",
            priority = TaskPriority.MEDIUM,
            isCompleted = true
        )
    )
}