# LCARS UI

[English](README.md)

[![](https://jitpack.io/v/SoluteToNight/LCARS-Compose-UI.svg)](https://jitpack.io/#SoluteToNight/LCARS-Compose-UI)

LCARS UI 是一个 Android Jetpack Compose 组件库和演示模板，用于构建受 Star Trek LCARS 风格启发的界面。

## 项目结构

- `:lcars-ui`：可复用的 LCARS Compose 组件、主题 token、动态部件和布局脚手架。
- `:app`：演示/起步应用，展示响应式主控台和组件目录。
- `example/`：可选的本地视觉参考文件目录，不提交到 Git。

## 安装

在 `settings.gradle.kts` 中加入 JitPack 仓库：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

添加 LCARS UI 依赖：

```kotlin
dependencies {
    implementation("com.github.SoluteToNight:LCARS-Compose-UI:<version>")
}
```

`<version>` 可以使用 GitHub Release tag、短 commit hash，或 `master-SNAPSHOT`。

## 运行

```powershell
./gradlew.bat assembleDebug
./gradlew.bat installDebug
```

单元测试和编译检查：

```powershell
./gradlew.bat test
./gradlew.bat :app:compileDebugAndroidTestKotlin
```

## 包含内容

- LCARS 主题 token：颜色、字体、间距。
- 几何基础组件：按钮、条块、弯角、框架面板。
- 动态组件：告警横幅、状态灯、进度条、分段仪表、扫描扫线、读数滚动条。
- 数据/显示组件：遥测面板、数据表、日志控制台、数字矩阵、星图坐标等。
- 布局模板：app、PADD、console 和响应式脚手架。
- 演示应用：支持 portrait PADD、compact landscape、wide landscape 三种断点。

## 组件文档

- 中文组件说明：[lcars-ui/COMPONENTS.zh-CN.md](lcars-ui/COMPONENTS.zh-CN.md)
- English component guide: [lcars-ui/COMPONENTS.en.md](lcars-ui/COMPONENTS.en.md)

## 字体策略

库默认打包 Antonio 作为类 LCARS 的窄体显示字体。Antonio 使用 SIL Open Font License，字体文件位于 `lcars-ui/src/main/res/font/antonio_variable.ttf`，许可证位于 `lcars-ui/src/main/assets/fonts/antonio/OFL.txt`。

应用仍然可以通过向 `LcarsTheme` 传入自定义 `LcarsTypography` 来注入其他已授权字体。

## 致谢

视觉参考和 LCARS 交互思路参考了 [louh/lcars](https://github.com/louh/lcars)、[joernweissenborn/lcars](https://github.com/joernweissenborn/lcars) 和 [The LCARS Website Template](https://www.thelcars.com/)。参考资料仅作为设计指引使用，不随本仓库分发。

## 基础用法

```kotlin
LcarsTheme {
    LcarsPaddScaffold(title = "sensor deck") {
        LcarsAlertBanner(message = "system nominal", active = false)
        LcarsTelemetryPanel(
            title = "primary telemetry",
            entries = listOf(
                LcarsTelemetryEntry("lat", "30.542314 n"),
                LcarsTelemetryEntry("fix", "high precision", LcarsTelemetryStatus.Normal),
            ),
        )
    }
}
```

## 当前范围

可复用模块通过 JitPack 发布为 Android AAR。Maven Central 发布暂不在范围内。
