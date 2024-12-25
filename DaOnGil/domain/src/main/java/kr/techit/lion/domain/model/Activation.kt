package kr.techit.lion.domain.model

sealed interface Activation {
    data object Loading: Activation
    data object Activate: Activation
    data object DeActivate: Activation
}