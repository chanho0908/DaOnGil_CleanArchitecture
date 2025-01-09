package kr.techit.lion.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.techit.lion.domain.model.AppTheme

interface AppThemeRepository {
    fun getAppTheme(): Flow<AppTheme>
    suspend fun saveAppTheme(appTheme: AppTheme)
}