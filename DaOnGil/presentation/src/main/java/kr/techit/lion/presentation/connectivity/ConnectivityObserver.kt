package kr.techit.lion.presentation.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun getFlow(): Flow<Status>

    enum class Status{
        Unavailable, Available, Losing, Lost
    }
}