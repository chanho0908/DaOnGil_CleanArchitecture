package kr.techit.lion.domain.exception

sealed class HttpError : NetworkError() {
    data object BadRequestError : HttpError()
    data object AuthenticationError : HttpError()
    data object AuthorizationError : HttpError()
    data object NotFoundError : HttpError()
    data object ServerError : HttpError()
}