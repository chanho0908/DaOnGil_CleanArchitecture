package kr.techit.lion.convention

import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getBundle
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class DaongilDataConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(
            "daongil.android.library",
            "daongil.hilt.library",
            "daongil.android.room",
            "daongil.android.moshi"
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