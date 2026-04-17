package com.group7.buckeyeadventure

import com.group7.buckeyeadventure.data.model.GameTask
import com.group7.buckeyeadventure.domain.TASK_PAGE_SIZE
import com.group7.buckeyeadventure.domain.paginateTasks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskPagerTest {
    @Test
    fun firstPageReturnsOnlyPageSizeTasks() {
        val tasks = sampleTasks(25)

        val page = paginateTasks(tasks, requestedPage = 0)

        assertEquals(TASK_PAGE_SIZE, page.tasks.size)
        assertEquals("task-1", page.tasks.first().id)
        assertEquals("task-3", page.tasks.last().id)
        assertEquals(9, page.totalPages)
        assertFalse(page.canGoPrevious)
        assertTrue(page.canGoNext)
    }

    @Test
    fun lastPageReturnsRemainingTasks() {
        val tasks = sampleTasks(25)

        val page = paginateTasks(tasks, requestedPage = 8)

        assertEquals(1, page.tasks.size)
        assertEquals("task-25", page.tasks.first().id)
        assertEquals("task-25", page.tasks.last().id)
        assertTrue(page.canGoPrevious)
        assertFalse(page.canGoNext)
    }

    @Test
    fun requestedPageIsClampedToValidRange() {
        val tasks = sampleTasks(3)

        val page = paginateTasks(tasks, requestedPage = 99)

        assertEquals(0, page.pageIndex)
        assertEquals(1, page.totalPages)
        assertEquals(3, page.tasks.size)
    }

    private fun sampleTasks(count: Int): List<GameTask> =
        (1..count).map { index ->
            GameTask(
                id = "task-$index",
                title = "Task $index",
                progress = index
            )
        }
}
