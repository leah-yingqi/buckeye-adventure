package com.group7.buckeyeadventure

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.ui.localization.AppLanguage
import com.group7.buckeyeadventure.ui.screens.login.LoginScreen
import com.group7.buckeyeadventure.ui.screens.settings.SettingsScreen
import com.group7.buckeyeadventure.ui.screens.tasks.TaskBoardContent
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BuckeyeAdventureComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loginRequiresUsernameAndPassword() {
        composeRule.setContent {
            BuckeyeAdventureTheme {
                LoginScreen(onLoginSuccess = {})
            }
        }

        composeRule.onNodeWithTag("loginButton").performClick()

        composeRule.onNodeWithTag("loginError").assertIsDisplayed()
        composeRule.onNodeWithText("Please enter username & password.").assertIsDisplayed()
    }

    @Test
    fun loginAcceptsNonBlankCredentials() {
        var loginCount = 0
        composeRule.setContent {
            BuckeyeAdventureTheme {
                LoginScreen(onLoginSuccess = { loginCount++ })
            }
        }

        composeRule.onNodeWithTag("usernameField").performTextInput("brutus")
        composeRule.onNodeWithTag("passwordField").performTextInput("buckeye")
        composeRule.onNodeWithTag("loginButton").performClick()

        assertEquals(1, loginCount)
    }

    @Test
    fun loginSettingsButtonOpensSettings() {
        var settingsOpenCount = 0
        composeRule.setContent {
            BuckeyeAdventureTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onOpenSettings = { settingsOpenCount++ }
                )
            }
        }

        composeRule.onNodeWithTag("settingsButton").performClick()

        assertEquals(1, settingsOpenCount)
    }

    @Test
    fun taskBoardPagesTasksAndCompletesSelection() {
        val completionUpdates = mutableListOf<Pair<String, Boolean>>()
        composeRule.setContent {
            BuckeyeAdventureTheme {
                TaskBoardContent(
                    tasks = sampleTasks(12),
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
        composeRule.onNodeWithTag("nextPageButton").assertIsEnabled().performClick()

        composeRule.onNodeWithTag("taskItem-task-4").assertIsDisplayed()
        composeRule.onNodeWithTag("taskItem-task-1").assertDoesNotExist()
        composeRule.onNodeWithTag("taskItem-task-4").performClick()
        composeRule.onNodeWithTag("completeTaskButton").performClick()

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

        composeRule.onNodeWithText("Mark as uncompleted").assertIsDisplayed()
        composeRule.onNodeWithTag("completeTaskButton").performClick()

        assertEquals(listOf("task-1" to false), completionUpdates)
    }

    @Test
    fun settingsSwitchUpdatesDarkModeState() {
        var darkModeEnabled = false
        composeRule.setContent {
            BuckeyeAdventureTheme(darkTheme = darkModeEnabled) {
                SettingsScreen(
                    darkModeEnabled = darkModeEnabled,
                    onDarkModeChange = { darkModeEnabled = it },
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("Dark mode").assertIsDisplayed()
        composeRule.onNodeWithTag("darkModeSwitch").performClick()

        composeRule.runOnIdle {
            assertEquals(true, darkModeEnabled)
        }
    }

    @Test
    fun settingsCanChangeLanguage() {
        var selectedLanguage = AppLanguage.English
        composeRule.setContent {
            BuckeyeAdventureTheme {
                SettingsScreen(
                    darkModeEnabled = false,
                    onDarkModeChange = {},
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    onBack = {}
                )
            }
        }

        composeRule.onNodeWithText("Language").assertIsDisplayed()
        composeRule.onNodeWithTag("language-Chinese").performClick()

        composeRule.runOnIdle {
            assertEquals(AppLanguage.Chinese, selectedLanguage)
        }
    }

    private fun sampleTasks(count: Int): List<GameTask> =
        (1..count).map { index ->
            GameTask(
                id = "task-$index",
                title = "Task $index",
                description = "Visit campus stop $index",
                rewardCoins = index * 5,
                progress = index
            )
        }
}
