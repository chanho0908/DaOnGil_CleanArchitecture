package kr.techit.lion.convention

import com.android.build.gradle.LibraryExtension
import kr.techit.lion.convention.extension.Plugins.ANDROID_LIBRARY
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.configureKotlinAndroid
import kr.techit.lion.convention.extension.configureKotlinCoroutine
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(ANDROID_LIBRARY)

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            configureKotlinCoroutine(this)
        }
    }
)