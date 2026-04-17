package com.group7.buckeyeadventure.ui.localization

import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage(val displayName: String) {
    English("English"),
    Chinese("简体中文")
}

data class AppStrings(
    val appName: String,
    val username: String,
    val password: String,
    val login: String,
    val loginError: String,
    val gameTasks: String,
    val back: String,
    val settings: String,
    val tasks: String,
    val page: String,
    val of: String,
    val previous: String,
    val next: String,
    val selectTask: String,
    val progress: String,
    val reward: String,
    val coins: String,
    val done: String,
    val markCompleted: String,
    val markUncompleted: String,
    val appearance: String,
    val darkMode: String,
    val darkModeDescription: String,
    val language: String,
    val languageDescription: String
)

val LocalAppStrings = staticCompositionLocalOf { stringsFor(AppLanguage.English) }

fun stringsFor(language: AppLanguage): AppStrings =
    when (language) {
        AppLanguage.English -> AppStrings(
            appName = "Buckeye Adventure",
            username = "Username",
            password = "Password",
            login = "Log in",
            loginError = "Please enter username & password.",
            gameTasks = "Game Tasks",
            back = "Back",
            settings = "Settings",
            tasks = "Tasks",
            page = "Page",
            of = "of",
            previous = "Prev",
            next = "Next",
            selectTask = "Select a task to view details",
            progress = "Progress",
            reward = "Reward",
            coins = "coins",
            done = "Done",
            markCompleted = "Mark as completed",
            markUncompleted = "Mark as uncompleted",
            appearance = "Appearance",
            darkMode = "Dark mode",
            darkModeDescription = "Use a darker color theme.",
            language = "Language",
            languageDescription = "Choose the app language."
        )

        AppLanguage.Chinese -> AppStrings(
            appName = "Buckeye Adventure",
            username = "用户名",
            password = "密码",
            login = "登录",
            loginError = "请输入用户名和密码。",
            gameTasks = "游戏任务",
            back = "返回",
            settings = "设置",
            tasks = "任务",
            page = "第",
            of = "页，共",
            previous = "上一页",
            next = "下一页",
            selectTask = "选择一个任务查看详情",
            progress = "进度",
            reward = "奖励",
            coins = "金币",
            done = "完成",
            markCompleted = "标记为完成",
            markUncompleted = "标记为未完成",
            appearance = "外观",
            darkMode = "深色模式",
            darkModeDescription = "使用更暗的颜色主题。",
            language = "语言",
            languageDescription = "选择应用语言。"
        )
    }
