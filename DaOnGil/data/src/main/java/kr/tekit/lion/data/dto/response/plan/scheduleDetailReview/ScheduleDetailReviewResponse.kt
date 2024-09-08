package kr.tekit.lion.data.dto.response.plan.scheduleDetailReview

import kr.tekit.lion.domain.model.ScheduleDetailReview

internal data class ScheduleDetailReviewResponse(
    val code: Int,
    val data: Data,
    val message: String
){
    fun toDomainModel(): ScheduleDetailReview {
        return ScheduleDetailReview(
            reviewId = data.reviewId,
            content = data.content,
            grade = data.grade,
            imageList = data.imageList,
            isWriter = data.isWriter,
            hasReview = data.hasReview,
            profileUrl = data.profileUrl
        )
    }
}