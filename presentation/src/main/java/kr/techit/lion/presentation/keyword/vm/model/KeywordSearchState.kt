package kr.techit.lion.presentation.keyword.vm.model

import kr.techit.lion.domain.model.search.RecentlySearchKeywordList
import kr.techit.lion.presentation.connectivity.ConnectivityObserver.Status

data class KeywordSearchState (
    val keywordList: RecentlySearchKeywordList = RecentlySearchKeywordList(emptyList()),
    val inputStatus: KeywordInputStatus = KeywordInputStatus.Initial,
)