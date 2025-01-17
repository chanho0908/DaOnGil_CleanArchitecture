package kr.techit.lion.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.techit.lion.data.database.AppSettings
import kr.techit.lion.data.database.dataStore
import kr.techit.lion.domain.model.AppTheme
import javax.inject.Inject

class AppThemeDataSource @Inject constructor(
    private val context: Context,
) {
    private val dataStore: DataStore<AppSettings>
        get() = context.dataStore

    val appTheme: Flow<AppTheme>
        get() = dataStore.data.map { it.appTheme }

    suspend fun saveAppTheme(appTheme: AppTheme) {
        dataStore.updateData { it.copy(appTheme = appTheme) }
    }
}