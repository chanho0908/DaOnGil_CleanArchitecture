package kr.techit.lion.presentation.login.model

sealed class UserState {
    object Checking: UserState()
    object ExistingUser: UserState()
    object NewUser: UserState()
}