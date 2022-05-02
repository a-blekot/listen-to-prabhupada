plugins {
    kotlin("multiplatform")
    id("base-kotlin-convention")
}

kotlin {
    android()
    ios()

    sourceSets {
//        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        named("commonTest") {
            dependencies {
//
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
//                implementation(libs.test.common)
//                implementation(libs.test.annotations)
            }
        }

        named("androidTest") {
            dependencies {
//                implementation(kotlin("test-junit"))
//                implementation("junit:junit:4.13.2")
//                implementation(libs.test.junit)
            }
        }
    }
}
