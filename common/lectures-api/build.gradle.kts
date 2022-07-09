plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.common.database)
                implementation(projects.common.networkApi)
                implementation(projects.common.utils)

                implementation(libs.decompose.decompose)
            }
        }
    }
}
