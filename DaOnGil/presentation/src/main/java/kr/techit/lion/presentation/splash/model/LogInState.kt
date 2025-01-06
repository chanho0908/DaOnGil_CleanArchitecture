package kr.techit.lion.presentation.splash.model

sealed class LogInStatus {
    data object Checking : LogInStatus()
    data object LoggedIn : LogInStatus()
    data object LoginRequired : LogInStatus()
}