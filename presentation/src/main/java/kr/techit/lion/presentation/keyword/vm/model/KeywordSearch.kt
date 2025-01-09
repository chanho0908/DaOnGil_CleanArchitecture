package kr.techit.lion.presentation.keyword.vm.model

import kr.techit.lion.domain.model.search.Arrange
import kr.techit.lion.domain.model.search.ListSearchOption

data class KeywordSearch(
    val keyword: String,
    val page: Int,
){
    fun toDomainModel(): ListSearchOption {
        return ListSearchOption(
            category = null,
            page = page,
            size = 0,
            disabilityType = emptyList(),
            detailFilter = emptyList(),
            areaCode = null,
            sigunguCode = null,
            query = keyword,
            arrange = Arrange.SortByLatest.sortCode
        )
    }
}
