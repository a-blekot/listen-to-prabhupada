plugins {
    id("android-library-convention")
    id("kotlin-multiplatform-convention")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:database"))
                implementation(project(":common:network-api"))
                implementation(project(":common:utils"))

                implementation(libs.decompose.decompose)
            }
        }
    }
}
