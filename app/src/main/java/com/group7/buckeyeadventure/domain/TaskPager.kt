package com.group7.buckeyeadventure.domain

import com.group7.buckeyeadventure.data.model.GameTask
import kotlin.math.ceil
import kotlin.math.max

const val TASK_PAGE_SIZE = 3

data class TaskPage(
    val tasks: List<GameTask>,
    val pageIndex: Int,
    val totalPages: Int,
    val totalTasks: Int
) {
    val canGoPrevious: Boolean = pageIndex > 0
    val canGoNext: Boolean = pageIndex < totalPages - 1
}

fun paginateTasks(
    tasks: List<GameTask>,
    requestedPage: Int,
    pageSize: Int = TASK_PAGE_SIZE
): TaskPage {
    require(pageSize > 0) { "pageSize must be greater than zero" }

    val totalPages = max(1, ceil(tasks.size / pageSize.toDouble()).toInt())
    val pageIndex = requestedPage.coerceIn(0, totalPages - 1)
    val start = pageIndex * pageSize
    val end = (start + pageSize).coerceAtMost(tasks.size)
    val pageTasks = if (start < tasks.size) tasks.subList(start, end) else emptyList()

    return TaskPage(
        tasks = pageTasks,
        pageIndex = pageIndex,
        totalPages = totalPages,
        totalTasks = tasks.size
    )
}
