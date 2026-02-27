package com.group7.buckeyeadventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.group7.buckeyeadventure.ui.theme.BuckeyeAdventureTheme
import android.util.Log
import androidx.navigation.NavController
import com.group7.buckeyeadventure.ui.AppRoot

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

//Implemented in a separate file
@Composable
fun GameScreen(navController: NavController) {

}
