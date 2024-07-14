package kr.tekit.lion.domain.repository

import kr.tekit.lion.domain.model.AreaCode
import kr.tekit.lion.domain.model.AreaCodeList

interface AreaCodeRepository {
    suspend fun getAreaCodeByName(areaName: String): String?
    suspend fun getAllAreaCodes(): AreaCodeList
    suspend fun addAreaCodeInfo(areaCodeList: List<AreaCode>)
}