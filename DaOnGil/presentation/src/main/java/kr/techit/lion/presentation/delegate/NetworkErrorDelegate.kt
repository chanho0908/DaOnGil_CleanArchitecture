package kr.techit.lion.presentation.delegate

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.techit.lion.domain.exception.HttpError.AuthenticationError
import kr.techit.lion.domain.exception.HttpError.AuthorizationError
import kr.techit.lion.domain.exception.HttpError.BadRequestError
import kr.techit.lion.domain.exception.HttpError.NotFoundError
import kr.techit.lion.domain.exception.HttpError.ServerError
import kr.techit.lion.domain.exception.NetworkError
import kr.techit.lion.domain.exception.NetworkError.ConnectError
import kr.techit.lion.domain.exception.NetworkError.TimeoutError
import kr.techit.lion.domain.exception.NetworkError.UnknownError
import kr.techit.lion.domain.exception.NetworkError.UnknownHostError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkErrorDelegate @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Loading)
    val networkState: StateFlow<NetworkState> get() = _networkState.asStateFlow()

    fun handleUsecaseNetworkError(e: Throwable): NetworkError {
        return when (e) {
            is ConnectException -> ConnectError
            is SocketTimeoutException -> TimeoutError
            is UnknownHostException -> UnknownHostError
            is HttpException -> {
                when (e.code()) {
                    400 -> BadRequestError
                    401 -> AuthenticationError
                    403 -> AuthorizationError
                    404 -> NotFoundError
                    else -> ServerError
                }
            }
            else -> UnknownError
        }
    }

    private fun asUiText(exception: NetworkError): String{
        val uiTextProvider = UiTextProvider(context).asUiText(exception)
        return uiTextProvider
    }

    fun handleNetworkError(exception: NetworkError) {
        _networkState.value = NetworkState.Error(asUiText(exception))
    }

    fun handleNetworkSuccess(){
        _networkState.value = NetworkState.Success
    }

    fun handleNetworkLoading(){
        _networkState.value = NetworkState.Loading
    }
}

sealed class NetworkState{
    data object Loading: NetworkState()
    data object Success: NetworkState()
    data class Error(val msg: String): NetworkState()
}