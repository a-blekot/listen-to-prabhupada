plugins {
    id("com.android.library")
    id("kotlin-base-convention")
    id("android-base-convention")
    id("dev.icerock.mobile.multiplatform-resources")
    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.common.data)
    implementation(projects.common.downloadsApi)
    implementation(projects.common.favoritesApi)
    implementation(projects.common.filtersApi)
    implementation(projects.common.playerApi)
//    implementation(projects.common.resources)
    implementation(projects.common.resultsApi)
    implementation(projects.common.root)
    implementation(projects.common.settings)
    implementation(projects.common.settingsApi)
    implementation(projects.common.utils)

    implementation(libs.bundles.androidx.compose)
    implementation(libs.accompanist.insets)
    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extCompose)
    debugImplementation(libs.napier.android.debug)
    releaseImplementation(libs.napier.android.release)
}