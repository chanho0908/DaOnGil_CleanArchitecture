package kr.tekit.lion.domain.model

data class ConcernType(
    val isPhysical: Boolean,
    val isHear: Boolean,
    val isVisual: Boolean,
    val isElderly: Boolean,
    val isChild: Boolean,
)