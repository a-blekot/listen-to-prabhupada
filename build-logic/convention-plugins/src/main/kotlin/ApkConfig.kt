import org.gradle.api.JavaVersion

object ApkConfig {
    const val APPLICATION_ID = "com.listentoprabhupada"
    const val APPLICATION_ID_SUFFIX = ".dev"

    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 32
    const val COMPILE_SDK_VERSION = 32

    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"

    val JAVA_VERSION = JavaVersion.VERSION_11
    val JAVA_VERSION_NAME = JavaVersion.VERSION_11.toString()
}