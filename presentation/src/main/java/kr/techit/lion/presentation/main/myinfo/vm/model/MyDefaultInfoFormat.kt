package kr.techit.lion.presentation.main.myinfo.vm.model

enum class MyDefaultInfoFormat(
    private val value: String
) {
    DATE("%d일"),
    NAME("%s님"),
    REVIEW("%d개의 리뷰");

    override fun toString(): String = value

    companion object{
        fun toDateFormat(date: Int): String {
            return DATE.value.format(date)
        }

        fun toNameFormat(name: String): String {
            return NAME.value.format(name)
        }

        fun toReviewFormat(reviewNum: Int): String {
            return REVIEW.value.format(reviewNum)
        }
    }
}