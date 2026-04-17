package com.group7.buckeyeadventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import android.util.Log
import com.group7.buckeyeadventure.ui.AppRoot
import com.group7.buckeyeadventure.ui.localization.AppLanguage
import com.group7.buckeyeadventure.ui.localization.LocalAppStrings
import com.group7.buckeyeadventure.ui.localization.stringsFor

private const val TAG_LIFE = "Lifecycle"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_LIFE, "MainActivity onCreate")
        enableEdgeToEdge()
        setContent {
            var darkModeEnabled by rememberSaveable { mutableStateOf(false) }
            var selectedLanguage by rememberSaveable { mutableStateOf(AppLanguage.English) }
            BuckeyeAdventureTheme(darkTheme = darkModeEnabled) {
                CompositionLocalProvider(
                    LocalAppStrings provides stringsFor(selectedLanguage)
                ) {
                    AppRoot(
                        darkModeEnabled = darkModeEnabled,
                        onDarkModeChange = { darkModeEnabled = it },
                        selectedLanguage = selectedLanguage,
                        onLanguageChange = { selectedLanguage = it }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG_LIFE, "MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG_LIFE, "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG_LIFE, "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG_LIFE, "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_LIFE, "MainActivity onDestroy")
    }
}
