package com.group7.buckeyeadventure.ui.screens.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Simple task model for UI.
 */
data class GameTask(
    val id: String,
    val title: String,
    val description: String,
    val rewardCoins: Int,
    val progress: Int,   // 0..100
    val isCompleted: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTaskScreen(
    onBack: () -> Unit,
    onTaskCompleted: (GameTask) -> Unit = {}
) {
    // Demo data (replace with your real data source later)
    var tasks by remember {
        mutableStateOf(
            listOf(
                GameTask("t1", "Explore the Oval", "Walk around the Oval and find 3 landmarks.", 50, 20, false),
                GameTask("t2", "Library Run", "Visit Thompson Library and collect a clue.", 80, 60, false),
                GameTask("t3", "Boss Prep", "Gather items and be ready for the next challenge.", 120, 100, true),
            )
        )
    }

    var selectedId by remember { mutableStateOf(tasks.firstOrNull()?.id) }
    val selected = tasks.firstOrNull { it.id == selectedId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Tasks") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left: Task list
            Card(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
            ) {
                TaskList(
                    tasks = tasks,
                    selectedId = selectedId,
                    onSelect = { selectedId = it }
                )
            }

            // Right: Details
            Card(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight()
            ) {
                if (selected == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a task to view details")
                    }
                } else {
                    TaskDetails(
                        task = selected,
                        onComplete = { task ->
                            if (!task.isCompleted) {
                                val updated = task.copy(progress = 100, isCompleted = true)
                                tasks = tasks.map { if (it.id == task.id) updated else it }
                                onTaskCompleted(updated)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskList(
    tasks: List<GameTask>,
    selectedId: String?,
    onSelect: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskListItem(
                    task = task,
                    selected = task.id == selectedId,
                    onClick = { onSelect(task.id) }
                )
            }
        }
    }
}

@Composable
private fun TaskListItem(
    task: GameTask,
    selected: Boolean,
    onClick: () -> Unit
) {
    val border = if (selected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    }

    OutlinedCard(
        border = border,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                if (task.isCompleted) {
                    AssistChip(onClick = {}, label = { Text("Done") })
                } else {
                    AssistChip(onClick = {}, label = { Text("${task.progress}%") })
                }
            }

            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (task.progress.coerceIn(0, 100)) / 100f },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            Text("Reward: ${task.rewardCoins} coins", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun TaskDetails(
    task: GameTask,
    onComplete: (GameTask) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(task.title, style = MaterialTheme.typography.headlineSmall)

        Text(task.description, style = MaterialTheme.typography.bodyMedium)

        HorizontalDivider()

        Text("Progress", style = MaterialTheme.typography.titleSmall)
        LinearProgressIndicator(
            progress = { (task.progress.coerceIn(0, 100)) / 100f },
            modifier = Modifier.fillMaxWidth()
        )
        Text("${task.progress.coerceIn(0, 100)}%", style = MaterialTheme.typography.bodySmall)

        Text("Reward", style = MaterialTheme.typography.titleSmall)
        Text("${task.rewardCoins} coins", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onComplete(task) },
            enabled = !task.isCompleted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (task.isCompleted) "Completed" else "Mark as completed")
        }
    }
}