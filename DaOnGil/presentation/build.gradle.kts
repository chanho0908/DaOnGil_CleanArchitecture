plugins {
    alias(libs.plugins.daongil.presentation)
}

android {
    namespace = "kr.techit.lion.presentation"

    viewBinding {
        enable = true
    }
}