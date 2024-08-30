package kr.tekit.lion.data.dto.remote.request

import kr.tekit.lion.data.dto.remote.request.util.AdapterProvider.Companion.JsonAdapter
import kr.tekit.lion.domain.model.ConcernType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

internal data class ConcernTypeRequest(
    val isPhysical: Boolean,
    val isHear: Boolean,
    val isVisual: Boolean,
    val isElderly: Boolean,
    val isChild: Boolean
)

fun ConcernType.toRequestBody(): RequestBody {
    return JsonAdapter(ConcernTypeRequest::class.java).toJson(
        ConcernTypeRequest(
            this.isPhysical,
            this.isHear,
            this.isVisual,
            this.isElderly,
            this.isChild
        )
    ).toRequestBody("application/json".toMediaTypeOrNull())
}