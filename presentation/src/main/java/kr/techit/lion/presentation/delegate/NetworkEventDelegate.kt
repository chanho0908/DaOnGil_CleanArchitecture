package kr.techit.lion.presentation.delegate

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.techit.lion.domain.exception.HttpError.ServerError
import kr.techit.lion.domain.exception.HttpError.NotFoundError
import kr.techit.lion.domain.exception.HttpError.AuthenticationError
import kr.techit.lion.domain.exception.HttpError.AuthorizationError
import kr.techit.lion.domain.exception.HttpError.BadRequestError
import kr.techit.lion.domain.exception.NetworkError
import kr.techit.lion.domain.exception.NetworkError.TimeoutError
import kr.techit.lion.domain.exception.NetworkError.ConnectError
import kr.techit.lion.domain.exception.NetworkError.UnknownError
import kr.techit.lion.domain.exception.NetworkError.UnknownHostError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class NetworkEventDelegate @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _event = Channel<NetworkEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun submitThrowableEvent(scope: CoroutineScope, e: Throwable) {
        when (e) {
            is TimeoutException -> event(scope, NetworkEvent.Error(asUiText(TimeoutError)))
            is UnknownHostException -> event(scope, NetworkEvent.Error(asUiText(UnknownHostError)))
            is UnknownError -> event(scope, NetworkEvent.Error(asUiText(UnknownError)))
            else -> event(scope, NetworkEvent.Error(asUiText(e)))
        }
    }

    fun asUiText(exception: Throwable): String{
        return UiTextProvider(context).asUiText(exception)
    }

    fun event(scope: CoroutineScope, event: NetworkEvent) {
        scope.launch {
            _event.send(event)
        }
    }
}

sealed class NetworkEvent{
    data object Loading: NetworkEvent()
    data object Success: NetworkEvent()
    data class Error(val msg: String): NetworkEvent()
}