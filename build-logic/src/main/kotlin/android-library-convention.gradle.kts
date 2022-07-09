import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.library")
    id("base-android-convention")
    id("base-kotlin-convention")
}

configure<BaseExtension> {
    sourceSets {
        getByName("main") {
            java.srcDirs("src/androidMain/kotlin")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}