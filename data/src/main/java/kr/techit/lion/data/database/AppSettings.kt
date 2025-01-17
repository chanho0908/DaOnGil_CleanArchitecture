package kr.techit.lion.data.database

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kr.techit.lion.domain.model.Activation
import kr.techit.lion.domain.model.AppTheme
import java.io.InputStream
import java.io.OutputStream

@Serializable
internal data class AppSettings(
    val accessToken: String = "",
    val refreshToken: String = "",
    val activation: Activation = Activation.DeActivate,
    val appTheme: AppTheme = AppTheme.LOADING
)

internal object AppSettingsSerializer: Serializer<AppSettings> {
    override val defaultValue: AppSettings = AppSettings()

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        }catch (e: SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = AppSettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}