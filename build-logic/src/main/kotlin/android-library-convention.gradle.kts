import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.library")
    id("base-android-convention")
    id("base-kotlin-convention")
}

configure<BaseExtension> {
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}