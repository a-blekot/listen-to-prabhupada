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
            val commonMain by getting {
                kotlin.srcDirs("src/commonMain/kotlin")
            }

            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                kotlin.srcDirs("src/iosMain/kotlin")
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
            }
        }
    }
}
