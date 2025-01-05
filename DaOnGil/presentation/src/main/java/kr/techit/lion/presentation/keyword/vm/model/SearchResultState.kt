package kr.techit.lion.presentation.keyword.vm.model

import kr.techit.lion.presentation.main.search.vm.model.PlaceModel

data class SearchResultState (
    val place : List<PlaceModel> = emptyList(),
    val page: Int = 0,
    val isLastPage: Boolean = false
)