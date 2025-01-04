package kr.techit.lion.domain.model.search

data class RecentlySearchKeyword (
    val id: Long? = null,
    val keyword: String
)

data class RecentlySearchKeywordList(
    val value: List<RecentlySearchKeyword>
){
    fun isExistKeyword(keyword: String): Boolean {
        return value.map { it.keyword }.any { it ==  keyword}
    }

    fun findKeyword(keyword: String): Long {
        return value.find { it.keyword == keyword }?.id ?: 0L
    }
}

fun String.toRecentlySearchKeyword() = RecentlySearchKeyword(keyword = this)