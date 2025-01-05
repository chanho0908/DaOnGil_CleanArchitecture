package kr.techit.lion.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.techit.lion.domain.model.search.RecentlySearchKeyword
import kr.techit.lion.domain.model.search.RecentlySearchKeywordList

interface RecentlySearchKeywordRepository {
    val savedKeyword: Flow<RecentlySearchKeywordList>
    suspend fun insertKeyword(keyword: String)
    suspend fun deleteKeyword(id: Long)
    suspend fun deleteAllKeyword()
}