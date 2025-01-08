package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.ANDROIDX_NAVIGATION_SAFEARGS
import kr.techit.lion.convention.extension.Plugins.DAONGIL_ANDROID_LIBRARY_PLUGIN
import kr.techit.lion.convention.extension.Plugins.DAONGIL_HILT_PLUGIN
import kr.techit.lion.convention.extension.Plugins.PARCELIZE
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getBundle
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.ksp
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class DaongilPresentationConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(
            DAONGIL_ANDROID_LIBRARY_PLUGIN,
            DAONGIL_HILT_PLUGIN,
            ANDROIDX_NAVIGATION_SAFEARGS,
            PARCELIZE
        )

        dependencies {
            implementation(project(":domain"))

            implementation(libs.getBundle("androidx"))
            implementation(libs.getBundle("coroutines"))
            implementation(libs.getBundle("naver"))
            implementation(libs.getBundle("google"))
            implementation(libs.getLibrary("firebase.crashlytics.ktx"))
            implementation(libs.getLibrary("kakao.user"))

            implementation(libs.getLibrary("glide"))
            ksp(libs.getLibrary("glide.compiler"))

            implementation(libs.getLibrary("splash"))
            implementation(libs.getLibrary("photoview"))
            implementation(libs.getLibrary("flexbox"))
            implementation(libs.getLibrary("circleindicator"))
            implementation(libs.getLibrary("simple.rating.bar"))
            implementation(libs.getLibrary("droidsonroids"))
        }
    }
)