package com.group7.buckeyeadventure.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.group7.buckeyeadventure.ui.localization.LocalAppStrings

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onOpenSettings: () -> Unit = {}
) {
        val strings = LocalAppStrings.current
        var username by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var errorMsg by rememberSaveable { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(strings.appName, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(strings.username) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("usernameField")
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(strings.password) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("passwordField")
            )

            Spacer(Modifier.height(16.dp))

            if (errorMsg != null) {
                Text(
                    errorMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.testTag("loginError")
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        errorMsg = strings.loginError
                    } else {
                        errorMsg = null
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("loginButton")
            ) {
                Text(strings.login)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onOpenSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("settingsButton")
            ) {
                Text(strings.settings)
            }
        }
}
