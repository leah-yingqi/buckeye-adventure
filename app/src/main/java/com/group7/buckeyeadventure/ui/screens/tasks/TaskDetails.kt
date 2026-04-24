package com.group7.buckeyeadventure.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.ui.localization.LocalAppStrings

@Composable
internal fun TaskDetailsCard(
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

        //Spacer(Modifier.weight(1f))

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
