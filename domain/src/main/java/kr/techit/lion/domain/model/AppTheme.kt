package kr.techit.lion.domain.model

enum class AppTheme {
    LIGHT, HIGH_CONTRAST, SYSTEM, LOADING;

    companion object {
        fun getNewTheme(theme: AppTheme, isDarkTheme: Boolean): AppTheme{
            return when (theme) {
                LIGHT -> HIGH_CONTRAST
                HIGH_CONTRAST -> LIGHT
                SYSTEM -> {
                    if (isDarkTheme) LIGHT else HIGH_CONTRAST
                }
                LOADING -> return LOADING
            }
        }
    }
}
