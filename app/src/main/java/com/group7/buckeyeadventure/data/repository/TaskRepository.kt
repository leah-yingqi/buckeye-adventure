package com.group7.buckeyeadventure.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.group7.buckeyeadventure.data.model.GameTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

private const val TAG = "TaskRepository"

class TaskRepository {
    private val db = Firebase.firestore
    private val taskCollection = db.collection("tasks")

    // Create
    fun addTask(task: GameTask) {
        taskCollection.add(task)
    }

    // Read
    fun getTasks() = callbackFlow<List<GameTask>> {
        val snapshotListener = taskCollection.addSnapshotListener { snapshot, _ ->
            val tasks = snapshot?.documents?.map { doc ->
                doc.toObject(GameTask::class.java)?.copy(id = doc.id) ?: GameTask()
            } ?: emptyList()
            trySend(tasks)
        }
        awaitClose { snapshotListener.remove() }
    }.distinctUntilChanged()

    // Delete
    fun deleteTask(taskId: String) {
        taskCollection.document(taskId).delete()
    }

    fun updateTaskStatus(taskId: String, isCompleted: Boolean) {
        taskCollection.document(taskId)
            .update(
                "isCompleted", isCompleted,
                "progress", if (isCompleted) 100 else 0
            )
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to update task completion for $taskId", exception)
            }
    }
}
