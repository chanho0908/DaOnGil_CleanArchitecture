package kr.techit.lion.presentation.delegate

import android.content.Context
import kr.techit.lion.domain.exception.HttpError
import kr.techit.lion.domain.exception.NetworkError
import kr.techit.lion.presentation.R

class UiTextProvider(private val context: Context) {

    fun asUiText(throwable: Throwable): String {
        val error = throwable.toNetworkError()
            ?: return context.getString(R.string.network_error_unknown_title)
        return "${asTitle(error)}\n${asContent(error)}"
    }

    private fun Throwable.toNetworkError(): NetworkError? {
        return when (this) {
            is NetworkError -> this
            else -> null
        }
    }

    private fun asTitle(error: NetworkError): String {
        return when (error) {
            is NetworkError.ConnectError -> context.getString(R.string.network_error_connect_title)
            is NetworkError.TimeoutError -> context.getString(R.string.network_error_timeout_title)
            is NetworkError.UnknownHostError -> context.getString(R.string.network_error_unknown_host_title)
            is NetworkError.UnknownError -> context.getString(R.string.network_error_unknown_title)
            is HttpError.BadRequestError -> context.getString(R.string.http_error_bad_request_title)
            is HttpError.AuthenticationError -> context.getString(R.string.http_error_authentication_title)
            is HttpError.AuthorizationError -> context.getString(R.string.http_error_authorization_title)
            is HttpError.NotFoundError -> context.getString(R.string.http_error_not_found_title)
            is HttpError.ServerError -> context.getString(R.string.http_error_server_title)
        }
    }

    private fun asContent(error: NetworkError): String {
        return when (error) {
            is NetworkError.ConnectError -> context.getString(R.string.network_error_connect_title)
            is NetworkError.TimeoutError -> context.getString(R.string.network_error_timeout_message)
            is NetworkError.UnknownHostError -> context.getString(R.string.network_error_unknown_host_message)
            is NetworkError.UnknownError -> context.getString(R.string.network_error_unknown_message)
            is HttpError.BadRequestError -> context.getString(R.string.http_error_bad_request_message)
            is HttpError.AuthenticationError -> context.getString(R.string.http_error_authentication_message)
            is HttpError.AuthorizationError -> context.getString(R.string.http_error_authorization_message)
            is HttpError.NotFoundError -> context.getString(R.string.http_error_not_found_message)
            is HttpError.ServerError -> context.getString(R.string.http_error_server_message)
        }
    }
}
