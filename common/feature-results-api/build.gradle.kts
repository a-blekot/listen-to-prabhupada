plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:lectures-api"))
                implementation(project(":common:player-api"))
                implementation(project(":common:utils"))
            }
        }
    }
}