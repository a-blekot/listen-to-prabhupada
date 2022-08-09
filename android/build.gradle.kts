plugins {
    id("android-application-convention")
    id("kotlin-parcelize")
}

dependencies {
    implementation(projects.common.data)
    implementation(projects.common.database)
    implementation(projects.common.features.featureResultsApi)
    implementation(projects.common.features.featureFavoritesApi)
    implementation(projects.common.features.featureDownloadsApi)
    implementation(projects.common.filtersApi)
    implementation(projects.common.filtersImpl)
    implementation(projects.common.favoritesApi)
    implementation(projects.common.downloadsApi)
    implementation(projects.common.lecturesApi)
    implementation(projects.common.lecturesImpl)
    implementation(projects.common.networkApi)
    implementation(projects.common.networkImpl)
    implementation(projects.common.playerApi)
    implementation(projects.common.playerImpl)
    implementation(projects.common.root)
    implementation(projects.common.settings)
    implementation(projects.common.utils)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.appcompat)
    //Compose Utils
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.swiperefresh)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Logger
    implementation(libs.napier.android)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)

    implementation(libs.bundles.mvikotlin.bndl)
    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extCompose)
}

//detekt {
//    version = "1.0.0"
//    reports {
//        xml {
//            destination = file("$project.buildDir/reports/detekt/detekt.xml")
//        }
//    }
//}

//ktlint {
//    android.set(true)
//    version.set("0.46.1")
//    ignoreFailures.set(false)
//    reporters {
//        reporter(ReporterType.PLAIN)
//        reporter(ReporterType.CHECKSTYLE)
//    }
//    outputToConsole.set(true)
//}