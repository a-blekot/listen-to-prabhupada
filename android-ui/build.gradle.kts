plugins {
    id("com.android.library")
    id("kotlin-base-convention")
    id("android-base-convention")
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
    implementation(projects.common.features.featureDownloadsApi)
    implementation(projects.common.features.featureFavoritesApi)
    implementation(projects.common.features.featureResultsApi)
    implementation(projects.common.filtersApi)
    implementation(projects.common.resultsApi)
    implementation(projects.common.playerApi)
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