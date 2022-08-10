import ApkConfig.APPLICATION_ID
import ApkConfig.APPLICATION_ID_SUFFIX

plugins {
    kotlin("android")
    id("com.android.application")
    id("kotlin-base-convention")
    id("android-base-convention")
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
            isMinifyEnabled = false
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
        }
    }
}
