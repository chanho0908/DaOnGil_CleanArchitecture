package kr.tekit.lion.data.dto.response.plan.ScheduleDetailInfo

internal data class Data(
    val dailyList: List<Daily>,
    val endDate: String,
    val imageUrls: List<String>?,
    val isPublic: Boolean,
    val isWriter: Boolean,
    val remainDate: String?,
    val startDate: String,
    val title: String,
    val writerId: Long,
    val writerNickname: String
)
