/**
 * Tests run from IDE in included builds can't recognize root wrapper
 * https://youtrack.jetbrains.com/issue/IDEA-262528
 */
//tasks.withType<Wrapper>().configureEach {
//    distributionType = Wrapper.DistributionType.BIN
//    gradleVersion = "7.4.2"
//}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.bundles.pulagins)
    }
}
//
//subprojects {
//    // ./gradlew dependencyUpdates
//    // Report: build/dependencyUpdates/report.txt
//    apply(plugin ="com.github.ben-manes.versions")
//    apply(plugin ="io.gitlab.arturbosch.detekt")
//    apply(plugin ="org.jlleitschuh.gradle.ktlint")
//    apply(plugin ="com.github.plnice.canidropjetifier")
//}

//https://github.com/ben-manes/gradle-versions-plugin#rejectversionsif-and-componentselection
//fun isNonStable(version: String): Boolean {
//    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
//    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
//    val isStable = stableKeyword || regex.matches(version)
//    return isStable.not()
//}
//
//tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
//    rejectVersionIf {
//        isNonStable(candidate.version) && !isNonStable(currentVersion)
//    }
//}
//
//allprojects {
//    afterEvaluate {
//        project.extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>()?.let { ext ->
//            ext.sourceSets.removeAll { sourceSet ->
//                setOf(
//                    "androidAndroidTestRelease",
//                    "androidTestFixtures",
//                    "androidTestFixturesDebug",
//                    "androidTestFixturesRelease",
//                ).contains(sourceSet.name)
//            }
//        }
//    }
//}
//
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}