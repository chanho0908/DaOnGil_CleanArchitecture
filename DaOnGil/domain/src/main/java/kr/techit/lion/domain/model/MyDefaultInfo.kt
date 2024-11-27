package kr.techit.lion.domain.model

data class MyDefaultInfo (
    val date: Int = 0,
    val name: String = "",
    val profileImg: String = "",
    val reviewNum: Int = 0
){
    fun toNameFormat(): String = "${name}님"
    fun toReviewFormat(review: String): String = "${review}개의 리뷰"
    fun toRegisterDateFormat(): String = "${date}일"
}