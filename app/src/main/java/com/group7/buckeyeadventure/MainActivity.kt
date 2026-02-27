package com.group7.buckeyeadventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

private const val TAG_LIFE = "Lifecycle"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG_LIFE, "MainActivity onCreate")
        enableEdgeToEdge()
        setContent {
            BuckeyeAdventureTheme {
                AppRoot()
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
        Log.d(TAG_LIFE, "")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_LIFE, "MainActivity onDestroy")
    }
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {LoginScreen(navController)}
            composable("game") {GameScreen(navController)}
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    ScreenLifeLongger("LoginScreen")

}

@Composable
fun ScreenLifeLongger(x0: String) {
    TODO("Not yet implemented")
}

@Composable
fun GameScreen(navController: NavController) {

}
