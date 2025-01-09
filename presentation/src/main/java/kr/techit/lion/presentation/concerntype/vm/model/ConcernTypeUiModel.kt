package kr.techit.lion.presentation.concerntype.vm.model

import kr.techit.lion.domain.model.ConcernType

data class ConcernTypeUiModel(
    val selectedConcernTypes: List<ConcernTypes>,
){
    fun isSelectedType(concernTypes: ConcernTypes): Boolean{
        return selectedConcernTypes.contains(concernTypes)
    }

    fun removeType(concernTypes: ConcernTypes): List<ConcernTypes> {
        return selectedConcernTypes.filter { it != concernTypes }
    }

    fun toDomainModel(): ConcernType {
        return ConcernType(
            isPhysical = selectedConcernTypes.contains(ConcernTypes.Physical),
            isHear = selectedConcernTypes.contains(ConcernTypes.Hear),
            isVisual = selectedConcernTypes.contains(ConcernTypes.Visual),
            isElderly = selectedConcernTypes.contains(ConcernTypes.Elderly),
            isChild = selectedConcernTypes.contains(ConcernTypes.Child),
        )
    }
}

fun ConcernType.toUiModel(): ConcernTypeUiModel{
    val selectedConcernTypes = mutableListOf<ConcernTypes>()
    if (this.isHear) selectedConcernTypes.add(ConcernTypes.Hear)
    if (this.isPhysical) selectedConcernTypes.add(ConcernTypes.Physical)
    if (this.isVisual) selectedConcernTypes.add(ConcernTypes.Visual)
    if (this.isElderly) selectedConcernTypes.add(ConcernTypes.Elderly)
    if (this.isChild) selectedConcernTypes.add(ConcernTypes.Child)
    return ConcernTypeUiModel(
        selectedConcernTypes = selectedConcernTypes
    )
}