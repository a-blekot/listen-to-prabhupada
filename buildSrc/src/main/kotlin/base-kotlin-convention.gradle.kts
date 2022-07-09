import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ApkConfig.JAVA_VERSION_NAME

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JAVA_VERSION_NAME
        freeCompilerArgs += listOf("-Xjdk-release=$JAVA_VERSION_NAME")
// TODO e: K2 compiler does not support multi-platform projects yet, so please remove -Xuse-k2 flag
//        freeCompilerArgs += listOf("-Xuse-k2", "-Xjdk-release=11")
    }
}
