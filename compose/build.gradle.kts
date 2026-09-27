import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// 版本统一由根 build.gradle.kts 的 plugins 块声明（apply false），此处不写 version。
//
// 注意：这里**不能**再写 id("org.jetbrains.kotlin.android")。
//   AGP 9.x 在 android { buildFeatures { compose = true } } 时会自行应用 Kotlin
//   插件，重复声明会触发：
//     "Cannot add extension with name 'kotlin', as there is an extension already registered"
//   根 build.gradle.kts 里以 apply false 声明版本即可。
//   同理也不能直接写 kotlin { ... } 块，要配置“已存在”的那个扩展。
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.compose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.compose"
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

    buildFeatures {
        compose = true
    }
}

// AGP 9.x 已移除 android { kotlinOptions { ... } }，改用 KGP 的 compilerOptions。
extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension>("kotlin") {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.0")
    // 仅用于提供 Theme.Experiment 的 AppCompat 父主题（本地仓库已缓存）
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
}