package com.group7.buckeyeadventure

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.ui.screens.tasks.TaskBoardContent
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TaskBoardContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun taskBoardPagesTasksAndCompletesSelection() {
        val completionUpdates = mutableListOf<Pair<String, Boolean>>()
        composeRule.setContent {
            BuckeyeAdventureTheme {
                TaskBoardContent(
                    tasks = sampleTasks(),
                    onBack = {},
                    onUpdateTaskCompletion = { taskId, isCompleted ->
                        completionUpdates += taskId to isCompleted
                    }
                )
            }
        }

        composeRule.onNodeWithTag("taskItem-task-1").assertIsDisplayed()
        composeRule.onNodeWithTag("taskItem-task-4").assertDoesNotExist()
        composeRule.onNodeWithTag("pageLabel").assertIsDisplayed()
        composeRule.onNodeWithTag("previousPageButton").assertIsNotEnabled()

        composeRule.onNodeWithTag("nextPageButton")
            .assertIsEnabled()
            .performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("taskItem-task-4").assertIsDisplayed()
        composeRule.onNodeWithTag("taskItem-task-1").assertDoesNotExist()

        composeRule.onNodeWithTag("taskItem-task-4").performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("completeTaskButton").performClick()

        composeRule.waitForIdle()

        assertEquals(listOf("task-4" to true), completionUpdates)
    }

    @Test
    fun completedTaskCanBeMarkedUncompleted() {
        val completionUpdates = mutableListOf<Pair<String, Boolean>>()
        composeRule.setContent {
            BuckeyeAdventureTheme {
                TaskBoardContent(
                    tasks = listOf(
                        GameTask(
                            id = "task-1",
                            title = "Task 1",
                            description = "Visit campus stop 1",
                            rewardCoins = 5,
                            progress = 100,
                            isCompleted = true
                        )
                    ),
                    onBack = {},
                    onUpdateTaskCompletion = { taskId, isCompleted ->
                        completionUpdates += taskId to isCompleted
                    }
                )
            }
        }

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("completeTaskButton").performClick()

        composeRule.waitForIdle()

        assertEquals(listOf("task-1" to false), completionUpdates)
    }

    private fun sampleTasks(): List<GameTask> =
        (1..12).map { index ->
            GameTask(
                id = "task-$index",
                title = "Task $index",
                description = "Visit campus stop $index",
                rewardCoins = index * 5,
                progress = index
            )
        }
}
