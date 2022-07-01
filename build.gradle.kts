import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.android.gradlePlgn)
        classpath(libs.kotlin.gradlePlgn)
        classpath(libs.kotlin.serialization.gradlePlgn)
        classpath(libs.sqlDelight.gradlePlgn)

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

allprojects {
    afterEvaluate {
        project.extensions.findByType<KotlinMultiplatformExtension>()?.let { ext ->
            ext.sourceSets.removeAll { sourceSet ->
                setOf(
                    "androidAndroidTestRelease",
                    "androidTestFixtures",
                    "androidTestFixturesDebug",
                    "androidTestFixturesRelease",
                ).contains(sourceSet.name)
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}