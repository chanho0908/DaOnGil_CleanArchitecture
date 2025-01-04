package kr.techit.lion.domain.model
import kotlinx.serialization.Serializable

@Serializable
sealed interface Activation {
    @Serializable
    data object Loading : Activation

    @Serializable
    data object Activate : Activation

    @Serializable
    data object DeActivate : Activation
}
