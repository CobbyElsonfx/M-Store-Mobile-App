plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

import java.util.Properties

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}
val productionApiBaseUrl = "https://test.ihilengineeringltd.com/"
    .let { if (it.endsWith("/")) it else "$it/" }
val debugApiBaseUrl = localProperties.getProperty("debug.api.url")
    ?.let { raw -> if (raw.endsWith("/")) raw else "$raw/" }
val keystoreProperties = Properties().apply {
    val file = rootProject.file("keystore.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.momokotle.agent"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.momokotle.agent"
        minSdk = 26
        targetSdk = 36
        versionCode = 18
        versionName = "1.0.17"

        buildConfigField("String", "API_BASE_URL", "\"$productionApiBaseUrl\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (keystoreProperties.isNotEmpty()) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            // Optional local Laravel override: debug.api.url=http://YOUR_PC_IP:8000/
            if (debugApiBaseUrl != null) {
                buildConfigField("String", "API_BASE_URL", "\"$debugApiBaseUrl\"")
            }
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

configurations.configureEach {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.15.0")
        force("androidx.core:core:1.15.0")
        force("androidx.activity:activity:1.8.2")
        force("androidx.activity:activity-ktx:1.8.2")
        force("androidx.activity:activity-compose:1.8.2")
        force("androidx.lifecycle:lifecycle-runtime:2.8.7")
        force("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
        force("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
        force("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
        force("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
        force("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
        force("androidx.lifecycle:lifecycle-common:2.8.7")
        force("androidx.lifecycle:lifecycle-livedata-core:2.8.7")
        force("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.7")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    implementation(libs.coil.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.paystack.ui)

    debugImplementation(libs.androidx.ui.tooling.preview)

    testImplementation("junit:junit:4.13.2")
}
