import java.util.Properties

plugins {
    alias(libs.plugins.daongil.data)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val baseUrl = properties.getProperty("base_url") ?: ""
val emergencyBaseUrl = properties.getProperty("emergency_base_url")?:""
val emergencyApiKey = properties.getProperty("emergency_api_key")?:""
val aedBaseUrl = properties.getProperty("aed_base_url")?:""
val pharmacyBaseUrl = properties.getProperty("pharmacy_base_url")?:""
val korApiKey = properties.getProperty("kor_api_key")?:""
val naverMapBase = properties.getProperty("naver_map_base") ?: ""
val naverMapId = properties.getProperty("naver_map_id") ?: ""
val naverMapSecret = properties.getProperty("naver_map_secret") ?: ""

android {
    namespace = "kr.techit.lion.data"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "NAVER_MAP_BASE", "\"$naverMapBase\"")
        buildConfigField("String", "EMERGENCY_BASE_URL", "\"$emergencyBaseUrl\"")
        buildConfigField("String", "EMERGENCY_API_KEY", "\"$emergencyApiKey\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "AED_BASE_URL", "\"$aedBaseUrl\"")
        buildConfigField("String", "PHARMACY_BASE_URL", "\"$pharmacyBaseUrl\"")
        buildConfigField("String", "KOR_API_KEY", "\"$korApiKey\"")
        buildConfigField("String", "NAVER_MAP_ID", "\"$naverMapId\"")
        buildConfigField("String", "NAVER_MAP_SECRET", "\"$naverMapSecret\"")
    }


    buildFeatures {
        buildConfig = true
    }
}