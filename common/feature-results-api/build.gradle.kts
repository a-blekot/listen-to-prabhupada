plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.lecturesApi)
                implementation(projects.common.playerApi)
                implementation(projects.common.utils)
            }
        }
    }
}