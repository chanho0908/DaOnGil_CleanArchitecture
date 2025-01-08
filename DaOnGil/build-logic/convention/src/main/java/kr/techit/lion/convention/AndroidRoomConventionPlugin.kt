package kr.techit.lion.convention

import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import kr.techit.lion.convention.extension.Plugins.ANDROIDX_ROOM
import kr.techit.lion.convention.extension.Plugins.KSP
import kr.techit.lion.convention.extension.applyPlugins
import kr.techit.lion.convention.extension.getBundle
import kr.techit.lion.convention.extension.getLibrary
import kr.techit.lion.convention.extension.implementation
import kr.techit.lion.convention.extension.ksp
import kr.techit.lion.convention.extension.libs
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : BuildLogicConventionPlugin(
    block = {
        applyPlugins(KSP, ANDROIDX_ROOM)

        extensions.configure<KspExtension> {
            arg("room.generateKotlin", "true")
        }

        extensions.configure<RoomExtension>{
            schemaDirectory("$projectDir/schemas")
        }

        dependencies {
            implementation(libs.getBundle("room"))
            ksp(libs.getLibrary("room.compiler"))
        }
    }
)