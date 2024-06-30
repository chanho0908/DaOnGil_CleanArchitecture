package kr.tekit.lion.data.datasource

import kr.tekit.lion.data.dto.remote.request.AreaCodeRequest
import kr.tekit.lion.data.dto.remote.response.areacode.AreaCodeResponse
import kr.tekit.lion.data.service.KorWithService
import javax.inject.Inject

class KorWithDataSource @Inject constructor(
    private val korWithService: KorWithService
) {
    suspend fun getAreaInfoList(code: String = ""): AreaCodeResponse {
        return korWithService.getAreaCode(AreaCodeRequest(code).toRequestModel())
    }
}