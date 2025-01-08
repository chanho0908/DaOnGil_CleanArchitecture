package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.DAONGIL_ANDROID_APPLICATION_PLUGIN
import kr.techit.lion.convention.extension.Plugins.DAONGIL_FIREBASE_PLUGIN
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies
import kr.techit.lion.convention.extension.Plugins.DAONGIL_HILT_PLUGIN

class DaongilAppConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(
            DAONGIL_ANDROID_APPLICATION_PLUGIN,
            DAONGIL_FIREBASE_PLUGIN,
            DAONGIL_HILT_PLUGIN,
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