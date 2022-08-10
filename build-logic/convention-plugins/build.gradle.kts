plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlgn)
    implementation(libs.kotlin.gradlePlgn)
    implementation(libs.sqlDelight.gradlePlgn)
}

val rootDirProject = file("../")

kotlin {
    sourceSets.getByName("main").kotlin.srcDir("build-logic/src/main/kotlin")
}
