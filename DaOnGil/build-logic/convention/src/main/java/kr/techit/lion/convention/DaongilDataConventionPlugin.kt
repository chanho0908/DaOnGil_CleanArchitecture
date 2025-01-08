package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.DAONGIL_ANDROID_LIBRARY_PLUGIN
import kr.techit.lion.convention.extension.Plugins.DAONGIL_HILT_PLUGIN
import kr.techit.lion.convention.extension.Plugins.DAONGIL_MOSHI_PLUGIN
import kr.techit.lion.convention.extension.Plugins.DAONGIL_ROOM_PLUGIN
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getBundle
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class DaongilDataConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(
            DAONGIL_ANDROID_LIBRARY_PLUGIN,
            DAONGIL_HILT_PLUGIN,
            DAONGIL_ROOM_PLUGIN,
            DAONGIL_MOSHI_PLUGIN
        )

        dependencies {
            implementation(project(":domain"))

            implementation(libs.getLibrary("androidx.datastore"))
            implementation(libs.getLibrary("kotlinx.serialization.json"))

            implementation(libs.getLibrary("gson"))

            implementation(libs.getBundle("network"))
        }
    }
)