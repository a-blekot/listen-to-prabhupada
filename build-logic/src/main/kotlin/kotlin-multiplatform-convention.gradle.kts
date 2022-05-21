import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("multiplatform")
    id("base-kotlin-convention")
}

kotlin {
    android()

    if (providers.gradleProperty("include_ios").get().toBoolean()) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        if (providers.gradleProperty("include_ios").get().toBoolean()) {
            val iosMain by creating
            val iosTest by creating

            getByName("iosX64Main").dependsOn(iosMain)
            getByName("iosX64Test").dependsOn(iosTest)
            getByName("iosArm64Main").dependsOn(iosMain)
            getByName("iosArm64Test").dependsOn(iosTest)
            getByName("iosSimulatorArm64Main").dependsOn(iosMain)
            getByName("iosSimulatorArm64Test").dependsOn(iosTest)
        }
    }
}
