buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${findProperty("version.kotlin")}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${findProperty("version.kotlin")}")
        classpath("com.squareup.sqldelight:gradle-plugin:${findProperty("version.sql_delight")}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${findProperty("version.hilt")}")
        classpath("com.android.tools.build:gradle:7.2.0-beta04")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}