package kr.techit.lion.domain.model.search

data class ListSearchOption (
    val category: String? = null,
    val size: Int = 10,
    val page: Int = 0,
    val query: String? = null,
    val disabilityType: List<Long>? = null,
    val detailFilter: List<Long>? = null,
    val areaCode: String? = null,
    val sigunguCode: String? = null,
    val arrange: String = Arrange.SortByLatest.sortCode
)
