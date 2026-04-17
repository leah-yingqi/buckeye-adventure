package com.group7.buckeyeadventure

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.group7.buckeyeadventure.ui.screens.login.LoginScreen
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
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
}
