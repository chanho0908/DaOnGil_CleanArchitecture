package kr.tekit.lion.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.tekit.lion.domain.model.ListSearchOption
import kr.tekit.lion.domain.model.ListSearchResultList
import kr.tekit.lion.domain.model.MapSearchOption
import kr.tekit.lion.domain.model.Place
import kr.tekit.lion.domain.model.Result

interface PlaceRepository {
    suspend fun getSearchPlaceResultByList(request: ListSearchOption): Result<ListSearchResultList>

    fun getSearchPlaceResultByMap(request: MapSearchOption): Flow<List<Place>>
}