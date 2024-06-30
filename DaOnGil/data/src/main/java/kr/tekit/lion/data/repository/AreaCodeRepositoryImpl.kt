package kr.tekit.lion.data.repository

import kr.tekit.lion.daongil.data.dto.local.toDomainModel
import kr.tekit.lion.daongil.data.dto.local.toEntity
import kr.tekit.lion.data.datasource.AreaCodeDataSource
import kr.tekit.lion.domain.model.AreaCode
import kr.tekit.lion.domain.repository.AreaCodeRepository
import javax.inject.Inject

class AreaCodeRepositoryImpl @Inject constructor(
    private val areaCodeDataSource: AreaCodeDataSource,
) : AreaCodeRepository {

    // 이름으로 지역코드 검색
    override suspend fun getAreaCodeByName(areaName: String): String? {
        return areaCodeDataSource.getAreaCodeInfo(areaName)
    }

    // 로컬의 모든 지역코드
    override suspend fun getAllAreaCodes(): List<AreaCode> {
        return areaCodeDataSource.getAllAreaCodes().map { it.toDomainModel() }
    }

    override suspend fun addAreaCodeInfo(areaCodeList: List<AreaCode>) {
        areaCodeDataSource.addAreaCodeInfoList(areaCodeList.map {
            when(it.name){
                "서울" -> AreaCode(it.code, "서울특별시").toEntity()
                "인천" -> AreaCode(it.code, "인천광역시").toEntity()
                "부산" -> AreaCode(it.code, "부산광역시").toEntity()
                "대전" -> AreaCode(it.code, "대전광역시").toEntity()
                "대구" -> AreaCode(it.code, "대구광역시").toEntity()
                "광주" -> AreaCode(it.code, "광주광역시").toEntity()
                "울산" -> AreaCode(it.code, "울산광역시").toEntity()
                "제주도" -> AreaCode(it.code, "제주특별자치도").toEntity()
                else -> it.toEntity()
            }
        })
    }
}