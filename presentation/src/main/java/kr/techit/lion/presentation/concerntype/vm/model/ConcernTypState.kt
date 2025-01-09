package kr.techit.lion.presentation.concerntype.vm.model

data class ConcernTypState (
    val nickName: String = "",
    val savedConcernType: ConcernTypeUiModel = ConcernTypeUiModel(emptyList()),
    val selectedConcernType: ConcernTypeUiModel = ConcernTypeUiModel(emptyList())
){
    fun addSelectedType(selectedType: ConcernTypes): ConcernTypState{
        return this.copy(
            selectedConcernType = this.selectedConcernType.copy(
                selectedConcernTypes = this.selectedConcernType.selectedConcernTypes + selectedType
            )
        )
    }

    fun removeSelectedType(selectedType: ConcernTypes): ConcernTypState{
        return this.copy(
            selectedConcernType = this.selectedConcernType.copy(
                selectedConcernTypes = this.selectedConcernType.removeType(selectedType)
            )
        )
    }
}