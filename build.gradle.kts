buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(libs.android.gradlePlgn)
        classpath(libs.kotlin.gradlePlgn)
        classpath(libs.kotlin.serialization.gradlePlgn)
        classpath(libs.sqlDelight.gradlePlgn)
        classpath(libs.hilt.gradlePlgn)

        classpath(libs.versionsPlgn)
        classpath(libs.detektPlgn)
        classpath(libs.ktlintPlgn)
        classpath(libs.canidropjetifierPlgn)
    }
}

subprojects {
    apply(plugin ="com.github.ben-manes.versions")
    apply(plugin ="io.gitlab.arturbosch.detekt")
    apply(plugin ="org.jlleitschuh.gradle.ktlint")
    apply(plugin ="com.github.plnice.canidropjetifier")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}