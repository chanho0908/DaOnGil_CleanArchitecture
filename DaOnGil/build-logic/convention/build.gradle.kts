plugins {
    `kotlin-dsl`
}

group = "kr.techit.lion.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.androidx.room.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "daongil.android.application"
            implementationClass = "kr.techit.lion.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary"){
            id = "daongil.android.library"
            implementationClass = "kr.techit.lion.convention.AndroidLibraryPlugin"
        }
        register("javaLibrary"){
            id = "daongil.java.library"
            implementationClass = "kr.techit.lion.convention.JavaLibraryPlugin"
        }
        register("androidRoom"){
            id = "daongil.android.room"
            implementationClass = "kr.techit.lion.convention.AndroidRoomConventionPlugin"
        }
        register("hilt"){
            id = "daongil.hilt.library"
            implementationClass = "kr.techit.lion.convention.HiltConventionPlugin"
        }
        register("androidFirebase"){
            id = "daongil.android.firebase"
            implementationClass = "kr.techit.lion.convention.AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidMoshi"){
            id = "daongil.android.moshi"
            implementationClass = "kr.techit.lion.convention.MoshiConventionPlugin"
        }
        register("daongilApp"){
            id = "daongil.app"
            implementationClass = "kr.techit.lion.convention.DaongilAppConventionPlugin"
        }
        register("daongilData"){
            id = "daongil.data"
            implementationClass = "kr.techit.lion.convention.DaongilDataConventionPlugin"
        }
        register("daongilDomain"){
            id = "daongil.domain"
            implementationClass = "kr.techit.lion.convention.DaongilDomainConventionPlugin"
        }
        register("daongilPresentation"){
            id = "daongil.presentation"
            implementationClass = "kr.techit.lion.convention.DaongilPresentationConventionPlugin"
        }
    }
}
