package kr.techit.lion.domain.exception

sealed class NetworkError : Throwable(){
    data object ConnectError : NetworkError()
    data object TimeoutError : NetworkError()
    data object UnknownHostError : NetworkError()
    data object UnknownError : NetworkError()
}