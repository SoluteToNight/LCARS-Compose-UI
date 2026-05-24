# lcars-ui

[English](README.md)

`lcars-ui` 是可复用的 LCARS Compose UI 模块。它提供主题、基础几何组件、动态读数、数据面板和应用布局脚手架。

## 安装

```kotlin
dependencies {
    implementation("com.github.SoluteToNight:LCARS-Compose-UI:<version>")
}
```

该 artifact 通过 JitPack 发布，对应本模块的 release AAR。

## 组件文档

- 中文组件说明：[COMPONENTS.zh-CN.md](COMPONENTS.zh-CN.md)
- English component guide: [COMPONENTS.en.md](COMPONENTS.en.md)

## 主题

在 Compose 内容根部使用 `LcarsTheme` 或 `LcarsAdaptiveTheme`。它们会提供 `LocalLcarsColors`、`LocalLcarsTypography` 和 `LocalLcarsSpacing`。

套用视觉风格的最简单方式是传入 `LcarsStyle`：

```kotlin
LcarsAdaptiveTheme(style = LcarsStyle.NemesisBlueUltra) {
    LcarsConsoleScaffold(
        leftWingContent = { /* commands */ },
        mainDeckContent = { /* readouts */ },
    )
}
```

当前可用风格：

- `LcarsStyle.ClassicUltra`
- `LcarsStyle.LowerDecks`
- `LcarsStyle.LowerDecksPadd`
- `LcarsStyle.NemesisBlueUltra`

风格 token 会为主题下的所有 LCARS 组件设置默认颜色和间距。应用仍然可以显式覆盖：

```kotlin
LcarsTheme(
    style = LcarsStyle.LowerDecks,
    colors = customColors,
    spacing = customSpacing,
) {
    AppContent()
}
```

默认字体是 Antonio，一个开源窄体显示字体，使用 SIL Open Font License。应用可以通过传入自定义 `LcarsTypography` 来使用其他已授权的类 LCARS 字体。

## 核心组件

- `LcarsButton`：pill、start-rounded、end-rounded、rectangle 等命令控件。
- `LcarsBar`：带可选端帽和内嵌标签的水平 LCARS 条块。
- `LcarsElbow`：四方向 Canvas 弯角几何。
- `LcarsFramePanel`：带 LCARS 标题/页脚条的内容框架。
- `LcarsTelemetryPanel`：响应式遥测读数网格。
- `LcarsDataTable`：高密度状态表格。

## 动态组件

- `LcarsAlertBanner`
- `LcarsStatusLight`
- `LcarsProgressBar`
- `LcarsSegmentedMeter`
- `LcarsScannerSweep`
- `LcarsReadoutTicker`

告警效果使用 stepped keyframes，不使用平滑呼吸式淡入淡出。

## 扩展组件

- `LcarsCommandRail`
- `LcarsSegmentedControl`
- `LcarsToggle`
- `LcarsDialog`
- `LcarsLogConsole`
- `LcarsNumberMatrix`
- `LcarsStarCoords`
- `LcarsNumericLabel`
- `LcarsDividerGrid`
- `LcarsInspectBracket`
- `LcarsTargetScanner`
- `LcarsResponsiveScaffold`
- `LcarsTransmissionFrame`
- `LcarsStarChart`

## 布局脚手架

- `LcarsAppScaffold`：通用 app 外壳，包含顶部条、控制 rail、内容区域和可选页脚。
- `LcarsPaddScaffold`：适合竖屏 PADD 的外壳。
- `LcarsConsoleScaffold`：基于 `LcarsMainConsole` 的横屏 console 外壳。

## 示例

```kotlin
LcarsFramePanel(title = "dynamic states") {
    LcarsStatusLight(label = "sensor lock", active = true)
    LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    LcarsAlertBanner(message = "critical alert active", active = true)
}
```
