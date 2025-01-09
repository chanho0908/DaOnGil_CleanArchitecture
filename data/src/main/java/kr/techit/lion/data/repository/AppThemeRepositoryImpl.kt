package kr.techit.lion.data.repository

import kotlinx.coroutines.flow.Flow
import kr.techit.lion.data.datasource.AppThemeDataSource
import kr.techit.lion.domain.model.AppTheme
import kr.techit.lion.domain.repository.AppThemeRepository
import javax.inject.Inject

class AppThemeRepositoryImpl @Inject constructor(
    private val appThemeDataSource: AppThemeDataSource
): AppThemeRepository {

    override fun getAppTheme(): Flow<AppTheme> {
        return appThemeDataSource.appTheme
    }

    override suspend fun saveAppTheme(appTheme: AppTheme) {
        appThemeDataSource.saveAppTheme(appTheme)
    }
}