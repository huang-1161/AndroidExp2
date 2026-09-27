plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.tablelayout"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.tablelayout"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
}
