plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "kr.techit.lion.presentation"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.crashlytics.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.navigation.ktx)
    implementation(libs.navigation)

    implementation(libs.google.services.v441)
    implementation(libs.play.services.location)
    implementation(libs.simple.rating.bar)
    implementation(libs.droidsonroids)
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    implementation(libs.circleindicator)

    implementation(libs.naverMap)
    implementation(libs.navercorp)
    implementation(libs.kakao.user)
    implementation(libs.splash)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.core.jvm)

    implementation(libs.photoview)

    implementation (libs.flexbox)
}