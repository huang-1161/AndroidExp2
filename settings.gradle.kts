// ============================================================================
//  实验2 —— Android 界面布局  (myproj)
//
//  工程路径：D:\MobileDevLabSchool\exp2\myproj   （纯 ASCII，无特殊字符）
//
//  本机环境说明（重要）：
//  1) 本机 HTTPS 已损坏（schannel SEC_E_NO_CREDENTIALS / 连接超时），
//     但 **明文 HTTP 可用**。因此依赖只从下面两处获取：
//       · local-repo/            本地 Maven 仓库（约 270MB），由本机已有的
//                                Gradle 依赖缓存导出，绝大多数依赖由此满足；
//       · http://127.0.0.1:18080/ 本地 HTTP 转发代理（tools/mvn-proxy.js），
//                                把请求转发给华为云镜像，避开坏掉的 HTTPS。
//     构建前请先启动代理：   node tools/mvn-proxy.js 18080
//  2) dl.google.com / repo1.maven.org 的 HTTPS 均不可达，google() 与
//     mavenCentral() 仅作占位保留在末尾，正常情况下不会被命中。
//  3) Gradle 的 plugins{} DSL 会为“插件 marker”同时请求 .pom 和 .jar，
//     而 Maven 上的 marker 只有 .pom；mvn-proxy.js 会为这类请求合成一个
//     合法的空 jar，否则 Kotlin / Compose 插件无法解析。
//  4) constraintlayout 与 compose 系列不在本机任何缓存中，必须经代理下载。
// ============================================================================

pluginManagement {
    repositories {
        // 本地 Maven 仓库（离线可用），放在最前面
        maven {
            name = "LocalOfflineRepo"
            url = uri("local-repo")
            metadataSources {
                gradleMetadata()
                mavenPom()
            }
        }
        // 本地 HTTP 代理 -> 华为云镜像
        maven {
            name = "LocalHttpProxy"
            url = uri("http://127.0.0.1:18080/")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        maven {
            name = "LocalOfflineRepo"
            url = uri("local-repo")
            metadataSources {
                gradleMetadata()
                mavenPom()
            }
        }
        maven {
            name = "LocalHttpProxy"
            url = uri("http://127.0.0.1:18080/")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "myproj"

// 实验2 共 5 个界面任务，每个任务一个独立可运行的 app 模块
include(":linearlayout")      // 实验一：线性布局   —— 4x4 网格
include(":tablelayout")       // 实验二：表格布局   —— 菜单
include(":constraint-basic")  // 实验三：约束布局1  —— 计算器
include(":constraint-space")  // 实验四：约束布局2  —— 太空旅行
include(":compose")           // 实验五：Jetpack Compose —— 课程学习任务