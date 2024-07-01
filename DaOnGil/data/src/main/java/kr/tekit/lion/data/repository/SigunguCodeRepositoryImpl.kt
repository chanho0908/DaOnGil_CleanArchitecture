package kr.tekit.lion.data.repository

import kr.tekit.lion.data.database.entity.toDomainModel
import kr.tekit.lion.data.database.entity.toEntity
import kr.tekit.lion.data.datasource.AreaCodeDataSource
import kr.tekit.lion.data.datasource.SigunguCodeDatasource
import kr.tekit.lion.domain.model.SigunguCode
import kr.tekit.lion.domain.repository.SigunguCodeRepository
import javax.inject.Inject

class SigunguCodeRepositoryImpl @Inject constructor(
    private val sigunguCodeDatasource: SigunguCodeDatasource
): SigunguCodeRepository {

    override suspend fun getSigunguCodeByVillageName(villageName: String): String? {
        return sigunguCodeDatasource.getSigunguCodeByVillageName(villageName)
    }

    override suspend fun getAllSigunguCode(code: String): List<SigunguCode> {
        return sigunguCodeDatasource.getAllSigunguInfoList(code).map { it.toDomainModel() }
    }

    override suspend fun addSigunguCode(sigunguCode: List<SigunguCode>) {
        sigunguCodeDatasource.addSigunguCodeInfoList(sigunguCode.map { it.toEntity() })
    }
}