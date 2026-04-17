package com.group7.buckeyeadventure.ui.screens.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.ui.localization.LocalAppStrings

@Composable
internal fun TaskListCard(
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
            Text(
                "${strings.reward}: ${task.rewardCoins} ${strings.coins}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
