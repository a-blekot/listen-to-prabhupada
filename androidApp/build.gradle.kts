plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

val composeVersion = findProperty("version.compose") as String

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()

        applicationId = "com.anadi.prabhupadalectures.android"
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":shared"))

//    com.android.support:appcompat-v7
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0-alpha04")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    implementation("androidx.lifecycle:lifecycle-process:2.4.1")

    //desugar utils
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    //Compose
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    //Compose Utils
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.accompanist:accompanist-insets:0.23.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.0")
    //Coroutines
    val coroutinesVersion = findProperty("version.kotlinx.coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    //DI
    implementation("com.google.dagger:hilt-android:${findProperty("version.hilt")}")
    kapt("com.google.dagger:hilt-compiler:${findProperty("version.hilt")}")
    //Navigation
    implementation("cafe.adriel.voyager:voyager-navigator:${findProperty("version.voyager")}")
    implementation("cafe.adriel.voyager:voyager-transitions:${findProperty("version.voyager")}")
    implementation("cafe.adriel.voyager:voyager-androidx:${findProperty("version.voyager")}")
    implementation("cafe.adriel.voyager:voyager-hilt:${findProperty("version.voyager")}")

    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    //Logger
    implementation("io.github.aakira:napier-android-debug:2.4.0")

    implementation("com.google.android.exoplayer:exoplayer-core:2.17.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.17.1")

    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
}