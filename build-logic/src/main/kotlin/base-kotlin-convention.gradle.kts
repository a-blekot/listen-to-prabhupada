import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs += listOf("-Xjdk-release=11")
//        freeCompilerArgs += listOf("-Xjdk-release=1.8", "-opt-in=kotlin.RequiresOptIn")
// TODO e: K2 compiler does not support multi-platform projects yet, so please remove -Xuse-k2 flag
//        freeCompilerArgs += listOf("-Xuse-k2", "-Xjdk-release=11")
    }
}