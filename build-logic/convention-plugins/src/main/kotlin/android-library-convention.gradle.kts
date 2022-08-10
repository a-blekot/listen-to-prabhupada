plugins {
    id("com.android.library")
    id("kotlin-base-convention")
    id("android-base-convention")
}

android {
    sourceSets {
        getByName("main") {
            java.srcDirs("src/androidMain/kotlin")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}