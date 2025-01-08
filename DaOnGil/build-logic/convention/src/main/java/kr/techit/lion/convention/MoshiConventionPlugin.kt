package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.KOTLINX_SERIALIZATION
import kr.techit.lion.convention.extension.Plugins.KSP
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getBundle
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.ksp
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class MoshiConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(KSP, KOTLINX_SERIALIZATION)

        dependencies {
            implementation(libs.getBundle("moshi"))
            ksp(libs.getLibrary("moshi.codegen"))
        }
    }
)