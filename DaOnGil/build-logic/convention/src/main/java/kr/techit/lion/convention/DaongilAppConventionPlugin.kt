package kr.techit.lion.convention

import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class DaongilAppConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(
            "daongil.android.application",
            "daongil.android.firebase",
            "daongil.hilt.library"
        )

        dependencies {
            implementation(project(":data"))
            implementation(project(":domain"))
            implementation(project(":presentation"))

            implementation(libs.getLibrary("kakao.user"))
            implementation(libs.getLibrary("navercorp"))
        }
    }
)