package kr.techit.lion.presentation.main.myinfo.vm.model

import kr.techit.lion.domain.model.MyDefaultInfo
import kr.techit.lion.presentation.main.myinfo.vm.model.MyDefaultInfoFormat.Companion.toDateFormat
import kr.techit.lion.presentation.main.myinfo.vm.model.MyDefaultInfoFormat.Companion.toNameFormat
import kr.techit.lion.presentation.main.myinfo.vm.model.MyDefaultInfoFormat.Companion.toReviewFormat

data class MyInfoUiModel (
    val date: String = "",
    val name: String = "",
    val profileImg: String = "",
    val reviewNum: String = ""
)

fun MyDefaultInfo.toUiModel(): MyInfoUiModel{
    val dateFormat = toDateFormat(this.date)
    val nameFormat = toNameFormat(this.name)
    val reviewFormat = toReviewFormat(this.reviewNum)

    return MyInfoUiModel(
        date = dateFormat,
        name = nameFormat,
        profileImg = this.profileImg,
        reviewNum = reviewFormat
    )
}