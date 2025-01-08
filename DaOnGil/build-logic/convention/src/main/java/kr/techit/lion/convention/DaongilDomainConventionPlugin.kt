package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.KOTLINX_SERIALIZATION
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class DaongilDomainConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(
            "daongil.java.library",
            KOTLINX_SERIALIZATION
        )

        dependencies {
            implementation(libs.getLibrary("kotlinx.coroutines.core.jvm"))
            implementation(libs.getLibrary("kotlinx.serialization.json"))
        }
    }
)