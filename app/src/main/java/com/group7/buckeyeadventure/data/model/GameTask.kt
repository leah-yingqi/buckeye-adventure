package com.group7.buckeyeadventure.data.model

import com.google.firebase.firestore.PropertyName

data class GameTask(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val rewardCoins: Int = 0,
    val progress: Int = 0,
    @get:PropertyName("isCompleted")
    @set:PropertyName("isCompleted")
    var isCompleted: Boolean = false
)

