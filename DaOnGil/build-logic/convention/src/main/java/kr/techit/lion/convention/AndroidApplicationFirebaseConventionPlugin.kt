package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.FIREBASE_CRASHLYTICS
import kr.techit.lion.convention.extension.Plugins.GOOGLE_SERVICES
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getBundle
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationFirebaseConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(GOOGLE_SERVICES, FIREBASE_CRASHLYTICS)

        dependencies {
            val bom = libs.findLibrary("firebase-bom").get()
            implementation(platform(bom))
            implementation(libs.getBundle("firebase"))
        }
    }
)