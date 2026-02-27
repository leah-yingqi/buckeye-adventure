package com.group7.buckeyeadventure.ui.navigation

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Tasks : Route("tasks")
}