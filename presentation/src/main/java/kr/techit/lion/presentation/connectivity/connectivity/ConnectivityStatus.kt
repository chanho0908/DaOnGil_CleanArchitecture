package kr.techit.lion.presentation.connectivity.connectivity

import androidx.annotation.StringRes
import kr.techit.lion.presentation.R

sealed interface ConnectivityStatus {
    data object Loading: ConnectivityStatus
    data object Available: ConnectivityStatus
    data class OnLost(
        @StringRes val msg: Int = R.string.can_not_access_network
    ): ConnectivityStatus
}
