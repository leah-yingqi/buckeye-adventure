package com.group7.buckeyeadventure.ui.screens.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.domain.paginateTasks
import com.group7.buckeyeadventure.ui.localization.LocalAppStrings
import com.group7.buckeyeadventure.viewmodel.TaskViewModel

private val CompactTaskListHeight = 420.dp

/**
 * Simple task model for UI.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTaskScreen(
    viewModel: TaskViewModel = viewModel(),
    onBack: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()

    TaskBoardContent(
        tasks = tasks,
        onBack = onBack,
        onUpdateTaskCompletion = viewModel::updateTaskCompletion
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskBoardContent(
    tasks: List<GameTask>,
    onBack: () -> Unit,
    onUpdateTaskCompletion: (String, Boolean) -> Unit,
) {
    val strings = LocalAppStrings.current
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
    var pageIndex by rememberSaveable { mutableIntStateOf(0) }
    val page = remember(tasks, pageIndex) { paginateTasks(tasks, pageIndex) }
    val taskById = remember(tasks) { tasks.associateBy { it.id } }
    val selected = selectedId?.let { taskById[it] }
    val pageLabel = remember(page.pageIndex, page.totalPages) {
        "${strings.page} ${page.pageIndex + 1} ${strings.of} ${page.totalPages}"
    }

    LaunchedEffect(tasks, page.pageIndex) {
        pageIndex = page.pageIndex
        if (selectedId == null || selectedId !in taskById) {
            selectedId = page.tasks.firstOrNull()?.id
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.gameTasks) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(strings.back) }
                }
            )
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (maxWidth < 700.dp) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TaskListCard(
                        tasks = page.tasks,
                        selectedId = selectedId,
                        pageLabel = pageLabel,
                        canGoPrevious = page.canGoPrevious,
                        canGoNext = page.canGoNext,
                        onSelect = { selectedId = it },
                        onPreviousPage = { pageIndex -= 1 },
                        onNextPage = { pageIndex += 1 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(CompactTaskListHeight)
                    )
                    TaskDetailsCard(
                        selected = selected,
                        onUpdateTaskCompletion = onUpdateTaskCompletion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TaskListCard(
                        tasks = page.tasks,
                        selectedId = selectedId,
                        pageLabel = pageLabel,
                        canGoPrevious = page.canGoPrevious,
                        canGoNext = page.canGoNext,
                        onSelect = { selectedId = it },
                        onPreviousPage = { pageIndex -= 1 },
                        onNextPage = { pageIndex += 1 },
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxHeight()
                    )
                    TaskDetailsCard(
                        selected = selected,
                        onUpdateTaskCompletion = onUpdateTaskCompletion,
                        modifier = Modifier
                            .weight(0.55f)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskListCard(
    tasks: List<GameTask>,
    selectedId: String?,
    pageLabel: String,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        TaskList(
            tasks = tasks,
            selectedId = selectedId,
            pageLabel = pageLabel,
            canGoPrevious = canGoPrevious,
            canGoNext = canGoNext,
            onSelect = onSelect,
            onPreviousPage = onPreviousPage,
            onNextPage = onNextPage
        )
    }
}

@Composable
private fun TaskDetailsCard(
    selected: GameTask?,
    onUpdateTaskCompletion: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAppStrings.current
    Card(modifier = modifier) {
        if (selected == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(strings.selectTask)
            }
        } else {
            TaskDetails(
                task = selected,
                onCompletionChange = { task, isCompleted ->
                    onUpdateTaskCompletion(task.id, isCompleted)
                }
            )
        }
    }
}

@Composable
private fun TaskList(
    tasks: List<GameTask>,
    selectedId: String?,
    pageLabel: String,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onSelect: (String) -> Unit
) {
    val strings = LocalAppStrings.current
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strings.tasks,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = pageLabel,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag("pageLabel")
            )
        }
        HorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .testTag("taskList"),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = tasks,
                key = { it.id },
                contentType = { "task" }
            ) { task ->
                TaskListItem(
                    task = task,
                    selected = task.id == selectedId,
                    onClick = { onSelect(task.id) }
                )
            }
        }

        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onPreviousPage,
                enabled = canGoPrevious,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("previousPageButton")
            ) {
                Text(strings.previous, maxLines = 1, overflow = TextOverflow.Clip, softWrap = false)
            }
            OutlinedButton(
                onClick = onNextPage,
                enabled = canGoNext,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("nextPageButton")
            ) {
                Text(strings.next, maxLines = 1, overflow = TextOverflow.Clip, softWrap = false)
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
    val strings = LocalAppStrings.current
    val progress = if (task.isCompleted) 100 else task.progress.coerceIn(0, 100)
    val border = if (selected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    }

    OutlinedCard(
        border = border,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "${task.title}, $progress percent complete"
            }
            .testTag("taskItem-${task.id}")
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
                    AssistChip(onClick = {}, label = { Text(strings.done) })
                } else {
                    AssistChip(onClick = {}, label = { Text("$progress%") })
                }
            }

            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress / 100f },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            Text("${strings.reward}: ${task.rewardCoins} ${strings.coins}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun TaskDetails(
    task: GameTask,
    onCompletionChange: (GameTask, Boolean) -> Unit
) {
    val strings = LocalAppStrings.current
    val progress = if (task.isCompleted) 100 else task.progress.coerceIn(0, 100)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(task.title, style = MaterialTheme.typography.headlineSmall)

        Text(task.description, style = MaterialTheme.typography.bodyMedium)

        HorizontalDivider()

        Text(strings.progress, style = MaterialTheme.typography.titleSmall)
        LinearProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "${task.title} progress $progress percent"
                }
        )
        Text("$progress%", style = MaterialTheme.typography.bodySmall)

        Text(strings.reward, style = MaterialTheme.typography.titleSmall)
        Text("${task.rewardCoins} ${strings.coins}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { onCompletionChange(task, !task.isCompleted) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("completeTaskButton")
        ) {
            Text(if (task.isCompleted) strings.markUncompleted else strings.markCompleted)
        }
    }
}
