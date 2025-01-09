package kr.techit.lion.convention

import com.android.build.api.dsl.ApplicationExtension
import kr.techit.lion.convention.extension.Plugins.ANDROID_APPLICATION
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.configureKotlinAndroid
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.getVersion
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(ANDROID_APPLICATION)

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)

            with(defaultConfig) {
                targetSdk = libs.getVersion("targetSdk").requiredVersion.toInt()
                versionCode = libs.getVersion("versionCode").requiredVersion.toInt()
                versionName = libs.getVersion("versionName").requiredVersion
            }
        }

        dependencies {
            implementation(libs.getLibrary("androidx.core.ktx"))
        }
    }
)