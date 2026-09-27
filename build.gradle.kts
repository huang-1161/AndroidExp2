// 顶层构建脚本：只声明插件版本，不在此处 apply
plugins {
    id("com.android.application") version "9.3.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
}
