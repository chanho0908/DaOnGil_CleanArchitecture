package kr.techit.lion.domain.model

data class ConcernType(
    val isPhysical: Boolean,
    val isHear: Boolean,
    val isVisual: Boolean,
    val isElderly: Boolean,
    val isChild: Boolean,
) {
    companion object {
        fun create(): ConcernType {
            return ConcernType(
                isPhysical = false,
                isHear = false,
                isVisual = false,
                isElderly = false,
                isChild = false
            )
        }
    }

}

fun ConcernType.hasAnyTrue(): Boolean {
    return listOf(isPhysical, isHear, isVisual, isElderly, isChild).any { it }
}
