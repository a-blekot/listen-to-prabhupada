import java.util.Properties
import java.io.FileInputStream

plugins {
    id("android-application-convention")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    signingConfigs {
        maybeCreate( "config").apply {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    buildTypes {
        maybeCreate("debug").apply {
            signingConfig = signingConfigs.getByName("config")
        }

        maybeCreate("release").apply {
            signingConfig = signingConfigs.getByName("config")
        }
    }
}

dependencies {
    implementation(projects.androidUi)
    implementation(projects.common.data)
    implementation(projects.common.database)
    implementation(projects.common.downloadsApi)
    implementation(projects.common.favoritesApi)
    implementation(projects.common.filtersApi)
    implementation(projects.common.filtersImpl)
    implementation(projects.common.networkApi)
    implementation(projects.common.networkImpl)
    implementation(projects.common.playerApi)
    implementation(projects.common.playerImpl)
//    implementation(projects.common.resources)
    implementation(projects.common.resultsApi)
    implementation(projects.common.resultsImpl)
    implementation(projects.common.root)
    implementation(projects.common.settings)
    implementation(projects.common.settingsApi)
    implementation(projects.common.settingsImpl)
    implementation(projects.common.utils)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.appcompat)
    //Compose Utils
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.activity.compose)
    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Logger
    debugImplementation(libs.napier.android.debug)
    releaseImplementation(libs.napier.android.release)

    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)

    implementation(libs.bundles.mvikotlin.bndl)
    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extCompose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.crashlytics.ktx)

    implementation(libs.play.core)
    implementation(libs.billing)
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