import ApkConfig.COMPILE_SDK_VERSION
import ApkConfig.MIN_SDK_VERSION
import ApkConfig.TARGET_SDK_VERSION
import ApkConfig.VERSION_CODE
import ApkConfig.VERSION_NAME
import ApkConfig.JAVA_VERSION
import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(COMPILE_SDK_VERSION)
    defaultConfig {
        minSdk = MIN_SDK_VERSION
        targetSdk = TARGET_SDK_VERSION
        versionCode = VERSION_CODE
        versionName = VERSION_NAME

        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        maybeCreate("debug").apply {
            buildConfigField("boolean", "INTERNAL", "true")
//            multiDexEnabled = true
            isDebuggable = true
            isMinifyEnabled = false
        }

        maybeCreate("release").apply {
            buildConfigField("boolean", "INTERNAL", "false")
            isDebuggable = false
            isMinifyEnabled = true
            consumerProguardFile("proguard-rules.pro")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0-rc02"
    }
}
