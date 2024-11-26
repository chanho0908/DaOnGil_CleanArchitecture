package kr.techit.lion.domain.model

data class PersonalInfo (
    val userName: String,
    val nickname: String,
    val phone: String
){
    companion object{
        fun create(): PersonalInfo{
            return PersonalInfo(
                userName = "",
                nickname = "",
                phone = ""
            )
        }
    }
}