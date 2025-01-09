package kr.techit.lion.domain.model.search

sealed class Arrange(val sortCode: String) {
    data object SortByLatest : Arrange("A")
    data object SortByPopularity : Arrange("B")
    data object SortByLetter : Arrange("C")
}