package kr.techit.lion.domain.model

data class IceInfo(
    val bloodType: String,
    val birth: String,
    val disease: String,
    val allergy: String,
    val medication: String,
    val part1Rel: String,
    val part1Phone: String,
    val part2Rel: String,
    val part2Phone: String,
){
    companion object{
        fun create() = IceInfo(
            bloodType = "",
            birth = "",
            disease = "",
            allergy = "",
            medication = "",
            part1Rel = "",
            part1Phone = "",
            part2Rel = "",
            part2Phone = ""
        )
    }
}