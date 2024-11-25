package kr.techit.lion.domain.model

data class PersonalInfo (
    val useName: String,
    val nickname: String,
    val phone: String
){
    companion object{
        fun create(): PersonalInfo{
            return PersonalInfo(
                useName = "",
                nickname = "",
                phone = ""
            )
        }
    }
}