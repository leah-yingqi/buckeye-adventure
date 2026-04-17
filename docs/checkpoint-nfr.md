# Checkpoint NFR Evidence

## Performance NFR

The task board must compose no more than 3 task cards at a time, even when the backing task list contains more than 3 tasks.

Implementation:

- `TaskPager.kt` limits each visible page to `TASK_PAGE_SIZE = 3`.
- `TaskBoardContent` passes only the current page to the `LazyColumn`.
- The expected profiler result is lower CPU and memory use on the Game Tasks screen when the dataset has more than 3 tasks because fewer card nodes, progress indicators, and chips are composed at once.

Profiler snapshot plan:

1. Before change: open Game Tasks with at least 25 Firestore tasks and record CPU + memory while scrolling the list.
2. After change: repeat the same recording on Game Tasks with the same 25 tasks.
3. Report the percentage change from Android Studio Profiler. Target improvement: at least 5-10% lower CPU and/or memory during the same screen interaction.

## Accessibility And Resilience NFR

The login form and task board should remain usable after configuration changes and expose readable semantic labels for assistive technologies.

Implementation:

- Login field values and validation errors use `rememberSaveable`, so they survive rotation and activity recreation.
- Task page and selected task use `rememberSaveable`, so the task board state survives rotation and activity recreation.
- Task cards and progress details expose content descriptions with task title and completion percentage.

## Test Evidence

Unit tests:

- `TaskPagerTest.firstPageReturnsOnlyPageSizeTasks`
- `TaskPagerTest.lastPageReturnsRemainingTasks`
- `TaskPagerTest.requestedPageIsClampedToValidRange`

Compose UI tests:

- `BuckeyeAdventureComposeTest.loginRequiresUsernameAndPassword`
- `BuckeyeAdventureComposeTest.loginAcceptsNonBlankCredentials`
- `BuckeyeAdventureComposeTest.taskBoardPagesTasksAndCompletesSelection`

Commands:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebugAndroidTest
.\gradlew.bat connectedDebugAndroidTest
```

`connectedDebugAndroidTest` requires a connected device or running emulator.
