package kr.techit.lion.presentation.concerntype.vm.model

sealed interface ConcernTypes {
    data object Physical : ConcernTypes
    data object Hear : ConcernTypes
    data object Visual : ConcernTypes
    data object Elderly : ConcernTypes
    data object Child : ConcernTypes
}