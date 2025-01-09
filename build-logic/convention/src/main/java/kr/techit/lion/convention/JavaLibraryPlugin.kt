package kr.techit.lion.convention

import kr.techit.lion.convention.extension.Plugins.JAVA_LIBRARY
import kr.techit.lion.convention.extension.Plugins.KOTLIN_JVM
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.getVersion
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class JavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(KOTLIN_JVM)
                apply(JAVA_LIBRARY)
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(libs.getVersion("jdkVersion").requiredVersion.toInt())
            }

            dependencies {
                implementation(libs.getLibrary("javax.inject"))
            }
        }
    }
}