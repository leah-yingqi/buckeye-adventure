package com.group7.buckeyeadventure.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.UUID

/**
 * ScreenLifeLogger
 * - Put this inside each screen to log screen "enter/leave" and lifecycle events.
 * - "Enter" is logged when the Composable first enters composition.
 * - "Leave" is logged when the Composable leaves composition (e.g., navigate away / pop).
 */
@Composable
fun ScreenLifeLogger(
    screenName: String,
    tag: String = "ScreenLifeLogger",
    logLifecycleEvents: Boolean = true,
    content: @Composable () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Unique id helps you distinguish multiple instances (recomposition vs real leave/enter)
    val instanceId = remember { UUID.randomUUID().toString().take(8) }

    //Enter (first time in composition)
    LaunchedEffect(Unit) {
        Log.d(tag, "ENTER  screen=$screenName id=$instanceId owner=${lifecycleOwner.javaClass.simpleName}")
    }

    //Observe lifecycle events + Leave (dispose)
    DisposableEffect(lifecycleOwner, logLifecycleEvents) {
        val observer = if (logLifecycleEvents) {
            LifecycleEventObserver { _, event ->
                val e = when (event) {
                    Lifecycle.Event.ON_CREATE -> "ON_CREATE"
                    Lifecycle.Event.ON_START -> "ON_START"
                    Lifecycle.Event.ON_RESUME -> "ON_RESUME"
                    Lifecycle.Event.ON_PAUSE -> "ON_PAUSE"
                    Lifecycle.Event.ON_STOP -> "ON_STOP"
                    Lifecycle.Event.ON_DESTROY -> "ON_DESTROY"
                    else -> event.name
                }
                Log.d(tag, "LIFECYCLE $e  screen=$screenName id=$instanceId")
            }
        } else null

        if (observer != null) {
            lifecycleOwner.lifecycle.addObserver(observer)
        }

        onDispose {
            if (observer != null) {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
            Log.d(tag, "LEAVE  screen=$screenName id=$instanceId")
        }
    }

    //Render actual UI
    content()
}