package com.group7.buckeyeadventure

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.group7.buckeyeadventure.ui.localization.AppLanguage
import com.group7.buckeyeadventure.ui.screens.settings.SettingsScreen
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

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
}
