package com.group7.buckeyeadventure.data.model

data class GameTask(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val rewardCoins: Int = 0,
    val progress: Int = 0,
    val isCompleted: Boolean = false
)

