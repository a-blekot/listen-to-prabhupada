import ApkConfig.APPLICATION_ID
import ApkConfig.APPLICATION_ID_SUFFIX

plugins {
    id("com.android.application")
    id("base-android-convention")
    id("base-kotlin-convention")
    kotlin("android")
}

android {
    defaultConfig {
        applicationId = APPLICATION_ID
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = APPLICATION_ID_SUFFIX
            isShrinkResources = false
        }
        release {
            isShrinkResources = true
        }
    }
}
