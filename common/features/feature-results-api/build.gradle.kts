plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.resultsApi)
                implementation(projects.common.playerApi)
                implementation(projects.common.utils)
            }
        }
    }
}