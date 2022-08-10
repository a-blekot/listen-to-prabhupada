import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ApkConfig.JAVA_VERSION_NAME

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JAVA_VERSION_NAME
    }
}
