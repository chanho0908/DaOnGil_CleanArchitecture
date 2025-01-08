package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.HILT
import kr.techit.lion.convention.extension.Plugins.KSP
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.ksp
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(HILT, KSP)

        dependencies {
            implementation(libs.getLibrary("hilt.android"))
            ksp(libs.getLibrary("hilt.android.compiler"))
        }
    }
)