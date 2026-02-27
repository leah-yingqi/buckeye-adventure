package com.group7.buckeyeadventure.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.group7.buckeyeadventure.ui.components.ScreenLifeLogger
import com.group7.buckeyeadventure.ui.screens.login.LoginScreen
import com.group7.buckeyeadventure.ui.navigation.Route
import com.group7.buckeyeadventure.ui.screens.tasks.GameTaskScreen

@Composable
fun AppRoot() {
    val nav = rememberNavController()

    Scaffold { padding ->
        NavHost(
            navController = nav,
            startDestination = Route.Login.path,
            modifier = Modifier.padding(padding)
        ) {
            //login
            composable(Route.Login.path) {
                ScreenLifeLogger("Login") {
                    LoginScreen(
                        onLoginSuccess = { nav.navigate(Route.Tasks.path) }
                    )
                }
            }
            //game task
            composable(Route.Tasks.path) {
                ScreenLifeLogger("Tasks") {
                    GameTaskScreen(onBack = { nav.popBackStack() })
                }
            }
        }
    }
}