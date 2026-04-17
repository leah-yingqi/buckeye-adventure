# Checkpoint NFR Evidence

## Performance NFR

The task board must compose no more than 3 task cards at a time, even when the backing task list contains more than 3 tasks.

Implementation:

- `TaskPager.kt` limits each visible page to `TASK_PAGE_SIZE = 3`.
- `TaskBoardContent` passes only the current page to `TaskList`.
- `TaskList` renders only the current page in its `LazyColumn`.
- The expected profiler result is lower CPU and memory use on the Game Tasks screen when the dataset has more than 3 tasks because fewer card nodes, progress indicators, and chips are composed at once.
- `TaskRepository.getTasks()` uses `distinctUntilChanged()` so identical Firestore snapshots do not trigger avoidable UI updates.

Profiler snapshot plan:

1. Before change: open Game Tasks with at least 25 Firestore tasks and record CPU + memory while scrolling the list.
2. After change: repeat the same recording on Game Tasks with the same 25 tasks.
3. Report the percentage change from Android Studio Profiler. Target improvement: at least 5-10% lower CPU and/or memory during the same screen interaction.

## Accessibility, Usability, And Resilience NFR

The login form, task board, and settings screen should remain usable after configuration changes and expose readable semantic labels for assistive technologies.

Implementation:

- Login field values and validation errors use `rememberSaveable`, so they survive rotation and activity recreation.
- Task page and selected task use `rememberSaveable`, so the task board state survives rotation and activity recreation.
- Task cards and progress details expose content descriptions with task title and completion percentage.
- The task screen uses a compact phone layout and a wider two-pane layout for landscape/tablet widths.
- Dark mode and language settings use `rememberSaveable`, so they survive screen rotation.
- Settings are available from the login screen before authentication.

## Security And Maintainability NFR

Clients should not be able to create or delete tasks from the app UI. Task completion should be the only client-visible task mutation.

Implementation:

- The task screen does not expose add or delete controls.
- `TaskViewModel` and `TaskRepository` no longer expose front-end add/delete methods.
- Firestore rules should allow task reads and only permit updates to `isCompleted` and `progress`.
- Task UI code is split into focused files:
  - `GameTaskScreen.kt`
  - `TaskList.kt`
  - `TaskDetails.kt`

Recommended Firestore rule for task updates:

```javascript
allow update: if request.resource.data
  .diff(resource.data)
  .affectedKeys()
  .hasOnly(['isCompleted', 'progress'])
  && request.resource.data.isCompleted is bool
  && request.resource.data.progress is int
  && request.resource.data.progress >= 0
  && request.resource.data.progress <= 100;
```

## Localization NFR

The app supports in-app language selection from Settings.

Implementation:

- English and Simplified Chinese are available.
- Login, task, and settings screens use localized strings from `LocalAppStrings`.
- The language can be changed before login from the Settings screen.

## Test Evidence

Unit tests:

- `TaskPagerTest.firstPageReturnsOnlyPageSizeTasks`
- `TaskPagerTest.lastPageReturnsRemainingTasks`
- `TaskPagerTest.requestedPageIsClampedToValidRange`

Compose UI tests:

- `LoginScreenTest.loginRequiresUsernameAndPassword`
- `LoginScreenTest.loginAcceptsNonBlankCredentials`
- `LoginScreenTest.loginSettingsButtonOpensSettings`
- `TaskBoardContentTest.taskBoardPagesTasksAndCompletesSelection`
- `TaskBoardContentTest.completedTaskCanBeMarkedUncompleted`
- `SettingsScreenTest.settingsSwitchUpdatesDarkModeState`
- `SettingsScreenTest.settingsCanChangeLanguage`

Commands:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebugAndroidTest
.\gradlew.bat connectedDebugAndroidTest
```

`connectedDebugAndroidTest` requires a connected device or running emulator.
