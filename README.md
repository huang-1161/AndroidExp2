# 实验2 —— Android 界面布局（myproj）

> **本文件是给"新会话/新接手的人"看的交接说明。** 阅读本文件即可完全恢复上下文，无需任何对话历史。

---

## 0. 一句话现状

5 个实验模块的**全部源码已经写完**，工程位于 `D:\MobileDevLabSchool\exp2\myproj`。
构建链路的各种环境坑已全部定位并绕过（见第 4 节）。
**注意：构建前必须先启动依赖代理**（见第 3 节），否则 `constraintlayout` / `compose` 拉不到依赖。

---

## 1. 目录结构

```
D:\MobileDevLabSchool\exp2\
├─ myproj\                     ← Gradle 根工程（纯 ASCII 路径，勿改名带特殊字符）
│  ├─ settings.gradle.kts      仓库配置（local-repo + 本地 HTTP 代理）
│  ├─ build.gradle.kts         插件版本声明（apply false）
│  ├─ gradle.properties        JVM/AndroidX 配置
│  ├─ local.properties         sdk.dir=D:\AndroidStudio\SDK
│  ├─ local-repo\              ★ 本地 Maven 仓库（约 270MB，由本机 Gradle 缓存导出）
│  ├─ tools\mvn-proxy.js       ★ 依赖代理脚本（见第 3 节）
│  ├─ .gradle-home\            本工程专用的 GRADLE_USER_HOME（含 android-user-home、tmp）
│  ├─ linearlayout\            实验一：线性布局 4x4 网格
│  ├─ tablelayout\             实验二：表格布局 菜单
│  ├─ constraint-basic\        实验三：约束布局1 计算器
│  ├─ constraint-space\        实验四：约束布局2 太空旅行
│  └─ compose\                 实验五：Jetpack Compose 课程学习任务
├─ 3-Android布局.pdf            老师给的布局教学讲义
├─ 实验2_Android界面布局.pdf      ★ 实验要求（5 个任务，含界面截图）
├─ 实验2图片\                    ★ 实验四所需图片资源（6 张 png）
└─ 实验2图片.rar
```

## 2. 五个实验任务与实现要点

实验要求原文在 `实验2_Android界面布局.pdf`，5 个任务分别在 7 页上，每页一张界面截图。
截图已用像素分析精确测量过（颜色、控件坐标），实现严格对照截图。

| # | 模块 | 界面 | 实现要点 |
|---|------|------|---------|
| 1 | `linearlayout` | 4行×4列文字网格 | 垂直 LinearLayout 套 4 个横向 LinearLayout；列宽用 `layout_weight` 按参考图实测比例 **158:250:180:162** 分配；行高 `weight=1`；前两行亮字（`#FFFFFF`）、后两行暗字（`#828282`）；黑底 + 每格 1dp 浅灰边框（`drawable/cell_border.xml`，3dp 间距）；文字 `16sp` 居中；根布局 `fitsSystemWindows` |
| 2 | `tablelayout` | 菜单（Hello TableLayout） | `TableLayout` + `TableRow`；`android:stretchColumns="1"` 把快捷键列推到最右；标题行 `android:layout_span="2"` 跨两列；菜单项 Open/Save/Save As/Import/Export/Quit，快捷键 Ctrl-O/Ctrl-S/Ctrl-Shift-S/**（Import 行没有快捷键）**/Ctrl-E；Import/Export 行左侧各有一个被截断字符（截图中呈 "X"），该行内层 LinearLayout 的子 TextView **必须写 layout_width/height**，否则启动即崩；底色 `#232323` |
| 3 | `constraint-basic` | 计算器 | 外层 ConstraintLayout + 内层 ConstraintLayout；按键 4×4，**水平链 + 垂直链**；按键宽 `0dp`、**高固定 42dp**；按键区固定 370dp / marginTop 140dp，靠 `spread` 链平摊行距；第 4 列链必须是 `keyDiv→keyMul→keySub→keyAdd`；顶部深青标题栏 `#00695C`；输入框米黄 `#C0BF9E` 内容 "0.0" 右对齐；`Calculator.java` 提供可用四则运算交互 |
| 4 | `constraint-space` | 太空旅行 | 全 ConstraintLayout；顶部 3 标签（空间站/航班/火星车，各带图标）；中部两个深绿方块 `#2E7D32`（DCA / MARS）夹一个 `double_arrows` 图标；下方两个橙色胶囊（One Way 宽度 `wrap_content` 带白色圆形旋钮 / 1 Traveller）；`galaxy` 图垂直 bias **0.22** + 左侧 `rocket_icon`；底部通栏深绿 DEPART 按钮。图片来自 `实验2图片`，已复制进 `res/drawable`（另有 `knob_white.xml` 画旋钮） |
| 5 | `compose` | 课程学习任务清单 | `MainActivity.kt` 单文件；`ComponentActivity` + `setContent`；Material3；`Scaffold` + `LazyColumn`；标题"课程学习任务"深红 `#B71C1C`；`OutlinedTextField`(请输入学习任务) + 红色"添加"按钮；进度文案 `已完成：x / y`（全角冒号 + 斜杠两侧空格）；任务行 = `Card` + `Checkbox` + 标题 + 右侧"删除"；**已完成任务标题加删除线** `TextDecoration.LineThrough`；**初始 3 项、其中第 1 项"学习 Column 和 Row"已完成**（即 `1 / 3`）；空输入点"添加"会加入"复习 LazyColumn"（对应文档"添加任务"截图） |

## 3. 依赖代理（**现在可选**，只在需要"新"依赖时才要开）

> ⚠️ 9-22 更新：本工程的 `local-repo` 已经做成**自包含**（见第 12 节），
> 5 个模块**离线**就能构建，平时不需要再开代理。
> 只有将来新增了本机没有的依赖时，才需要按本节启动代理去补。

本机 **HTTPS 已损坏**（`schannel SEC_E_NO_CREDENTIALS 0x8009030E`，访问 dl.google.com / repo1.maven.org 全部超时），
但**明文 HTTP(80) 正常**。当初 `androidx.constraintlayout` 与 `androidx.compose.*` 不在本机任何缓存里，
只能联网获取，所以用一个本地 HTTP 代理把请求转给华为云镜像：

```powershell
cd D:\MobileDevLabSchool\exp2\myproj\tools
node mvn-proxy.js 18080        # 监听 http://127.0.0.1:18080/
```

`settings.gradle.kts` 里已配好 `maven { name = "LocalHttpProxy"; url = uri("http://127.0.0.1:18080/") }`。
代理脚本还会做一件关键的事：**为插件 marker 合成空 jar**（见第 4.4 节）。

验证代理活着：
```powershell
curl.exe -sS -o NUL -w "%{http_code}`n" http://127.0.0.1:18080/androidx/constraintlayout/constraintlayout/2.1.4/constraintlayout-2.1.4.pom
# 应输出 200
```

## 4. 环境坑清单（全部已绕过，勿"修复"）

### 4.1 目录名曾含 U+2011 隐形连字符
原路径 `D:\MobileDev‑Lab-School\` 里的连字符是 **U+2011（不换行连字符）**，不是 ASCII `-`（U+002D）。
Java 启动器在 Windows 用 GBK 解码命令行参数时会把它变成 `?`，导致 javac/Gradle 报
`InvalidPathException: Illegal char <?>`。`sun.jnu.encoding` 是只读的，改不掉。
**已于 2025-09-21 把目录改名为 `D:\MobileDevLabSchool\`（去掉连字符），问题根除。**
⚠️ **不要再把工程放回任何含非 ASCII 字符的路径。**

### 4.2 本机 Gradle 默认 JVM 环境
- `JAVA_HOME` 原本指向 `E:\develop\jdk`（JDK 23）；本工程构建统一用 **`D:\HeiMaDevelop\JDK\jdk17`**（JDK 17）。
- `ANDROID_HOME` / `ANDROID_SDK_ROOT` = `D:\AndroidStudio\SDK`（已装 platform android-36、build-tools 36.0.0 / 37.0.0）。
- AVD：`probe_api36`（`D:\AndroidStudio\AVD`）。

### 4.3 不能直接 `gradlew.bat`
工程里的 `gradlew.bat` 是从别处拷的，其 wrapper jar 缺 `IDownload` 类，会报
`NoClassDefFoundError: org/gradle/wrapper/IDownload`。**直接调用已解压好的 Gradle 发行版**：

```
C:\Users\27659\.gradle\wrapper\dists\gradle-9.7.1-bin\5pq2t7s7z7i0g0ob8nscgzg1a\gradle-9.7.1\bin\gradle.bat
```

### 4.4 Gradle `plugins{}` DSL 与"插件 marker"
Gradle 会为 `org.jetbrains.kotlin.plugin.compose` 这类插件请求 marker 的 **`.pom` 和 `.jar`**，
而 Maven 上的 marker **只有 pom、没有 jar** → 404 → Gradle 判定"插件不存在"。
`tools/mvn-proxy.js` 对 `*.gradle.plugin-*.jar` 的请求**合成一个合法空 zip**，Gradle 随后只靠 pom 里的
dependency 去取真正的插件 jar。这是让 Kotlin / Compose 插件能解析的关键。

### 4.5 AGP 9.x 会自行应用 Kotlin 插件
在 `android { buildFeatures { compose = true } }` 存在时，AGP 9.x **自己**应用 Kotlin 插件。
若模块里再写 `id("org.jetbrains.kotlin.android")`，会报
`Cannot add extension with name 'kotlin', as there is an extension already registered`。
所以 `compose/build.gradle.kts` 的 `plugins{}` **只放 `com.android.application` + `org.jetbrains.kotlin.plugin.compose`**，
Kotlin 版本在根 `build.gradle.kts` 用 `apply false` 声明。同理模块里不能直接写 `kotlin { ... }`，
要写 `extensions.configure<KotlinAndroidProjectExtension>("kotlin") { ... }`。

### 4.6 其它
- **XML 注释里不能出现 `--`**。我曾用 `<!-- ---------- 第 1 行 ---------- -->` 导致
  `parseDebugLocalResources` 报 `注册中不能包含字符串 "--"`。现已全部改成 `=====`。
- Gradle daemon 会抱怨 `Unable to set daemon's environment variables`，无害。
- `C:\Users\27659\.android` 不可写，AGP 会打印一条 analytics 警告，无害。

## 5. 构建命令（PowerShell）

```powershell
$proj = 'D:\MobileDevLabSchool\exp2\myproj'
$env:JAVA_HOME            = 'D:\HeiMaDevelop\JDK\jdk17'
$env:GRADLE_USER_HOME     = "$proj\.gradle-home"
$env:ANDROID_USER_HOME    = "$proj\.gradle-home\android-user-home"
$env:ANDROID_HOME         = 'D:\AndroidStudio\SDK'
$env:ANDROID_SDK_ROOT     = 'D:\AndroidStudio\SDK'
$env:TMP = "$proj\.gradle-home\tmp"; $env:TEMP = $env:TMP

$gradle = "C:\Users\27659\.gradle\wrapper\dists\gradle-9.7.1-bin\5pq2t7s7z7i0g0ob8nscgzg1a\gradle-9.7.1\bin\gradle.bat"

& cmd /c "`"$gradle`" --console=plain -p `"$proj`" `
  :linearlayout:assembleDebug :tablelayout:assembleDebug `
  :constraint-basic:assembleDebug :constraint-space:assembleDebug :compose:assembleDebug"
```

产物 APK：`myproj\<模块>\build\outputs\apk\debug\<模块>-debug.apk`

> ⚠️ `GRADLE_USER_HOME` **必须**设为本工程的 `.gradle-home`。
> 若指向 `C:\Users\27659\.gradle` 或 `D:\Gradle`，那两个目录里的
> `init.d\aliyun-mirrors.gradle` 会强制注入**已失效的 HTTPS** 阿里云仓库，构建必失败。

## 6. 模拟器验证

```powershell
& "D:\AndroidStudio\SDK\emulator\emulator.exe" -avd probe_api36
& "D:\AndroidStudio\SDK\platform-tools\adb.exe" install -r "<apk 路径>"
& "D:\AndroidStudio\SDK\platform-tools\adb.exe" shell am start -n <包名>/.MainActivity
```
包名分别为：`com.example.linearlayout` / `com.example.tablelayout` /
`com.example.constraintbasic` / `com.example.constraintspace` / `com.example.compose`

## 7. 构建结果（已验证）

> ⚠️ 下表是**第一次**构建（9-21）的记录。9-22 修完运行时 Bug 与界面偏差后重新构建过，
> 最新 APK 大小与校验值见第 10 节。

**5 个模块全部 `assembleDebug` 成功，APK 均已产出。** 实际执行结果：

```
BUILD SUCCESSFUL in 3m 21s
166 actionable tasks: 131 executed, 35 from cache
```

| 模块 | APK | 大小 |
|------|-----|------|
| linearlayout    | `linearlayout\build\outputs\apk\debug\linearlayout-debug.apk`             | 3,265,614 B |
| tablelayout     | `tablelayout\build\outputs\apk\debug\tablelayout-debug.apk`               | 3,264,414 B |
| constraint-basic| `constraint-basic\build\outputs\apk\debug\constraint-basic-debug.apk`     | 3,780,538 B |
| constraint-space| `constraint-space\build\outputs\apk\debug\constraint-space-debug.apk`     | 3,852,151 B |
| compose         | `compose\build\outputs\apk\debug\compose-debug.apk`                       | 8,992,861 B |

## 8. 模拟器验证：可以跑起来（关键是需要放宽权限）

> ⚠️ 本节结论已于 9-22 更新：模拟器**可以**运行，5 个 APK 的装机与截图比对已全部完成，
> 详见第 10 节。下面 1~3 条是当初在 `workspace-write` 沙箱下失败的过程记录，保留作排错参考。

在 DSH 的 `workspace-write` 沙箱下无法运行 Android 模拟器。已尝试并排除的原因：

1. 模拟器首次失败是因为它要把状态写到 `C:\Users\27659\.android\`（沙箱外，只读）→ 无限重试
   `Unexpected error while creating: ...emu-last-feature-flags.protobuf.lock (error: 5)`。
2. 已通过 `ANDROID_USER_HOME` / `ANDROID_SDK_HOME` / `ANDROID_AVD_HOME` 把状态目录改到
   `myproj\.android-home\`（可写），并在其中新建了一个只含配置、不带快照的精简 AVD
   `exp2_api36`（3.3GB 快照已避免复制）。
3. 这样改后模拟器能读到系统镜像（`Found systemPath ...\system-images\android-36\default\x86_64\`）、
   通过 `hasCompatibleHypervisor` 检查，但**打印完 netsim 配置后静默退出（exit 1）**，
   `adb devices` 始终为空，日志里没有任何 ERROR/FATAL。判断为沙箱阻断了
   hypervisor / 设备 / 端口等模拟器必需的资源。

**根因**：模拟器需要 Windows **命名管道**（AVD 配置里 `hw.gltransport = pipe`），并且要拉起
`netsimd` 子进程，受限沙箱会把这类 IPC 拦掉；表现为打印完 netsim 配置后静默退出
（exit `-36863`，日志里没有任何 ERROR/FATAL）。**解决**：在放宽权限（`danger-full-access`）
下启动模拟器即可，模拟器起来之后，普通权限下的 `adb` 一样能装机、启动、截图。做法：

```powershell
& "D:\AndroidStudio\SDK\emulator\emulator.exe" -avd probe_api36
& "D:\AndroidStudio\SDK\platform-tools\adb.exe" wait-for-device
# 依次安装并启动 5 个 APK
$root = 'D:\MobileDevLabSchool\exp2\myproj'
$map = @{
  'linearlayout'     = 'com.example.linearlayout'
  'tablelayout'      = 'com.example.tablelayout'
  'constraint-basic' = 'com.example.constraintbasic'
  'constraint-space' = 'com.example.constraintspace'
  'compose'          = 'com.example.compose'
}
foreach ($m in $map.Keys) {
  $apk = Get-ChildItem "$root\$m\build\outputs\apk\debug\*-debug.apk" | Select-Object -First 1
  & "D:\AndroidStudio\SDK\platform-tools\adb.exe" install -r $apk.FullName
  & "D:\AndroidStudio\SDK\platform-tools\adb.exe" shell am start -n "$($map[$m])/.MainActivity"
  Start-Sleep -Seconds 3
  & "D:\AndroidStudio\SDK\platform-tools\adb.exe" exec-out screencap -p > "$root\screenshot-$m.png"
}
```

比对基准：`实验2_Android界面布局.pdf` 第 3～7 页的界面截图。

## 9. 9-22 修复的运行时 Bug 与界面偏差

> 以下问题**编译期全部不报错**，是装到模拟器上逐个截图比对才发现的。

**两个真实 Bug**

1. `tablelayout` 一启动就闪退：`InflateException ... line #120: You must supply a layout_width attribute`。
   Import / Export 两行里套了一层横向 `LinearLayout`，它的子 `TextView` 没写 `layout_width/height`
   （`TableRow` 的直接子项可以省略，普通 `LinearLayout` 的子项不行）→ 已补 `wrap_content`。
2. `constraint-basic` 计算器第 4 列按键整体下移半行、`−` 与 `+` 重叠。
   第 4 列的垂直链断了：`keyMul.bottom` 指向 `keyAdd`（跳过 `keySub`）、`keySub.bottom` 指向 parent 底部
   → 已改为 `keyMul.bottom → keySub.top`、`keySub.bottom → keyAdd.top`，链变成完整的
   `keyDiv → keyMul → keySub → keyAdd`。

**界面偏差（对照讲义截图逐张修正）**

| 现象 | 修正 |
|------|------|
| 全部界面被状态栏 / 导航栏遮挡（DEPART 被导航栏盖住） | targetSdk 36 强制 edge-to-edge；4 个 XML 根布局加 `android:fitsSystemWindows="true"`，Compose 用 `Scaffold` 的 `innerPadding` |
| 线性布局第 1、4 列文字换行被裁 | 字号 20sp → 16sp；列宽权重按实测改为 **158 : 250 : 180 : 162** |
| 参考图单元格有浅灰边框 | 新增 `drawable/cell_border.xml`（黑底 + 1dp `#7D7D7D` 描边），单元格 3dp 间距 |
| 计算器按键被垂直链拉伸铺满整屏 | `CalcKey` 高度 0dp → **42dp**；按键区固定 370dp、`spread` 链平摊行距 |
| One Way 胶囊横跨整屏 | 宽度改 `wrap_content`（参考图约占屏宽 1/3），旋钮用 24dp 间距跟文字后 |
| 星系插图位置过低（约 83% 高度） | `layout_constraintVertical_bias` 0.85 → **0.22**（参考图约 43%） |
| 表格 Import 行多了 `Ctrl-I` | 参考图中该行没有快捷键，已删除 |
| 颜色偏差 | 按参考图像素取样：工具栏 `#00897B→#00695C`、按键 `#DCDCDC→#D3D3D3`、显示框 `#C7C6A6→#C0BF9E`、表格底 `#000000→#232323`、标题栏 `#8C8C8C→#999999`、太空绿 `#1B5E20→#2E7D32` |

## 10. 最新构建结果与验证产物（9-22）

```
BUILD SUCCESSFUL
```

| 模块 | APK | 大小 | sha256（前 12 位） |
|------|-----|------|--------------------|
| linearlayout    | `linearlayout\build\outputs\apk\debug\linearlayout-debug.apk`         | 3,540,932 B | `9D022E9C0C41` |
| tablelayout     | `tablelayout\build\outputs\apk\debug\tablelayout-debug.apk`           | 3,538,086 B | `24E69E55D24C` |
| constraint-basic| `constraint-basic\build\outputs\apk\debug\constraint-basic-debug.apk` | 4,082,286 B | `196843F9A595` |
| constraint-space| `constraint-space\build\outputs\apk\debug\constraint-space-debug.apk` | 4,152,279 B | `0009A3A9DCD8` |
| compose         | `compose\build\outputs\apk\debug\compose-debug.apk`                   | 8,992,861 B | `2A47A9FDA0A0` |

模拟器：`exp2_api36`（Pixel 7 / Android 16 / 1080×2400 / 420dpi），冷启动约 50 秒。
5 个 APK 均已 `adb install -r` 安装并启动，`adb logcat -b crash` 为空（无崩溃）。

实机截图（`myproj\screenshots\`）：

| 文件 | 内容 |
|------|------|
| `linearlayout.png` | 线性布局 4×4 网格 |
| `tablelayout.png` | 表格布局菜单 |
| `constraint-basic.png` | 约束布局 1 计算器 |
| `constraint-space.png` | 约束布局 2 太空旅行 |
| `compose.png` / `compose-add.png` / `compose-updated.png` | Compose 三种状态：`1 / 3` → 添加后 `1 / 4` → 勾选后 `3 / 4` |

装机截图的两个小技巧（避免截图被系统弹窗污染）：

```powershell
& $adb shell settings put global hide_error_dialogs 1   # 关掉 ANR/崩溃弹窗
& $adb shell input tap <x> <y>                          # 若已有 ANR 弹窗，点 Wait 关掉
```

## 11. 剩余待办

- [x] 5 个模块 `assembleDebug` 全部通过，APK 产出
- [x] 工程上下文写入 `README.md`（本文件）
- [x] 模拟器装机 + 截图比对（见第 8、10 节）
- [x] 实验报告文档：`D:\MobileDevLabSchool\exp2\实验2_实验报告.md`
- [ ] 可选：把实验报告导出为 Word / PDF 后提交；把 `screenshots\` 一并打包

## 12. `local-repo` 已做成自包含（离线可构建，9-22）

**背景**：Android Studio 里点 Run 反复报 `Connection refused: getsockopt` —— 那是 Gradle 连不上
`http://127.0.0.1:18080/`（本地依赖代理）。代理进程不稳定（一挂，IDE 构建就失败），
而且 IDE 默认的 Gradle user home（`D:\Gradle`）里**没有** `androidx.constraintlayout` 缓存。
为了彻底摆脱对代理和网络的依赖，把工程缓存里的依赖按 **Maven 目录规范**补进了 `local-repo`。

**做了什么**（全部本地操作，没有联网下载）：

1. 把 `.gradle-home\caches\modules-2\files-2.1\**` 转成 Maven 布局写进 `local-repo`
   （`<group 以 / 分隔>/<artifact>/<version>/<file>`），新增 **175 个文件 / 234 MB**；
   `local-repo` 从 737 文件 270 MB → **926 文件 528 MB**。
2. Compose 的 Android 构件在缓存里叫 `ui-release.aar` 这类名字，Maven 规范名是 `ui-android-1.6.8.aar`，
   Gradle 的 transform 阶段会按规范名再找一次 → 用 **NTFS 硬链接**补了 15 个规范名（不额外占空间）。
3. `local-repo` 里原本只差一个"插件标记"：`org.jetbrains.kotlin.plugin.compose.gradle.plugin:2.2.10`
   （它的真身 `org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.2.10` 已经在仓库里）。
   手工补了 marker 的 `.pom`（标准 marker 格式，dependency 指向真身）+ 一个 **22 字节的合法空 jar**
   （与 `tools/mvn-proxy.js` 合成的是同一份 base64）。
4. 没有动 `C:\Users\27659\.gradle`（它本身是指向 `D:\Gradle` 的 **junction**，数据都在 D 盘）。

**验证方式（最严格）**：用一个**全新的空 `GRADLE_USER_HOME`** + `--offline` + `--rerun-tasks`
（强制重新编译、重新打包，且完全禁止联网）：

```powershell
$env:GRADLE_USER_HOME='D:\MobileDevLabSchool\exp2\myproj\.gradle-home-offlinetest'   # 空目录
& cmd /c '"D:\Gradle\wrapper\dists\gradle-9.7.1-bin\5pq2t7s7z7i0g0ob8nscgzg1a\gradle-9.7.1\bin\gradle.bat" --console=plain --offline --rerun-tasks -p "D:\MobileDevLabSchool\exp2\myproj" :compose:assembleDebug'
# 结果：BUILD SUCCESSFUL in 2m 52s / 34 actionable tasks: 34 executed
```

5 个模块的 `--offline` 全量构建同样通过（`BUILD SUCCESSFUL in 2m 59s`）。

**结论 / 使用建议**：

- Android Studio 的 **Gradle user home 保持默认（`D:\Gradle`）即可**，不需要指向本工程，
  这样删掉本工程也**不会影响其它项目**；
- 平时构建**不需要**开代理；只有将来要引入本机没有的新依赖时，才启动 `tools/mvn-proxy.js` 补一次，
  然后按第 1 步把新依赖再补进 `local-repo`；
- 若在 IDE 里仍看到旧的 `Connection refused` 红字，那是**上一次构建的残留输出**，
  执行 `File → Sync Project with Gradle Files` 或重新 `Rebuild Project` 即可。