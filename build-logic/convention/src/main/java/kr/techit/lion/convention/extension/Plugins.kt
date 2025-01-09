package kr.techit.lion.convention.extension

internal object Plugins {
    const val JAVA_LIBRARY = "java-library"

    const val KOTLIN_JVM = "org.jetbrains.kotlin.jvm"
    const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
    const val KOTLINX_SERIALIZATION = "org.jetbrains.kotlin.plugin.serialization"

    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"

    const val ANDROIDX_ROOM = "androidx.room"
    const val ANDROIDX_NAVIGATION_SAFEARGS = "androidx.navigation.safeargs.kotlin"

    const val PARCELIZE = "org.jetbrains.kotlin.plugin.parcelize"

    const val HILT = "com.google.dagger.hilt.android"
    const val KSP = "com.google.devtools.ksp"

    const val GOOGLE_SERVICES = "com.google.gms.google-services"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase.crashlytics"

    const val DAONGIL_ANDROID_APPLICATION_PLUGIN = "daongil.android.application"
    const val DAONGIL_ANDROID_LIBRARY_PLUGIN = "daongil.android.library"
    const val DAONGIL_FIREBASE_PLUGIN = "daongil.android.firebase"
    const val DAONGIL_HILT_PLUGIN = "daongil.hilt.library"
    const val DAONGIL_ROOM_PLUGIN = "daongil.android.room"
    const val DAONGIL_MOSHI_PLUGIN = "daongil.android.moshi"
    const val DAONGIL_JAVA_PLUGIN = "daongil.java.library"
}