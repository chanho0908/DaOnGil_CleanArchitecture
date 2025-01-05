package kr.techit.lion.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kr.techit.lion.data.database.AppSettings
import kr.techit.lion.data.database.dataStore
import kr.techit.lion.domain.model.Activation
import javax.inject.Inject

internal class ActivationDataSource @Inject constructor(
    private val context: Context
){
    private val dataStore: DataStore<AppSettings>
        get() = context.dataStore

    private val data: Flow<AppSettings>
        get() = dataStore.data

    val activation get() = data.map { it.activation }

    suspend fun saveUserActivation(active: Activation) {
        dataStore.updateData {
            it.copy(
                activation = active
            )
        }
    }
}