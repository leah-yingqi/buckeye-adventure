package com.group7.buckeyeadventure.ui.screens.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.domain.paginateTasks
import com.group7.buckeyeadventure.ui.localization.LocalAppStrings
import com.group7.buckeyeadventure.viewmodel.TaskViewModel

private val CompactTaskListHeight = 420.dp

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
