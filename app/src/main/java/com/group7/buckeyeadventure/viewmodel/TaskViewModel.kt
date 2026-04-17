package com.group7.buckeyeadventure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel(){
    private val repository = TaskRepository()

    // UI observes the status
    val tasks: StateFlow<List<GameTask>> = repository.getTasks().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNewTask(title: String) {
        repository.addTask(GameTask(title = title))
    }

    fun updateTaskCompletion(taskId: String, isCompleted: Boolean) {
        // Call Repository to update Firebase
        // Firebase will trigger addSnapshotListener, update UI list automatically
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, isCompleted)
        }
    }

    fun deleteTask(taskId: String) {
        repository.deleteTask(taskId)
    }
}
