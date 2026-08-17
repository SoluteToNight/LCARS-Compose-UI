# lcars-ui

[English](README.md)

`lcars-ui` 是 Jetpack Compose LCARS 组件库。v1 保持单一 Android AAR，同时按主题、基础能力、控件、数据显示、布局、完整场景和 PADD 组件拆分职责包。

## 安装

```kotlin
dependencies {
    implementation(project(":lcars-ui"))
}
```

## 包结构

| 包 | 职责 |
| --- | --- |
| `com.lcars.ui.theme` | 主题规范、三个预设、语义色、尺寸、形状、动效、声音 |
| `com.lcars.ui.foundation` | 公共文本组件与底层文本能力 |
| `com.lcars.ui.controls` | 按钮、开关、分段控件、对话框、命令轨 |
| `com.lcars.ui.display` | 状态、进度、遥测、表格、日志、仪表和动态读数 |
| `com.lcars.ui.layout` | 条块、弯角、框架、响应式脚手架和控制台布局 |
| `com.lcars.ui.scene` | 星图、通信界面等完整场景模式 |
| `com.lcars.ui.padd` | 手持 PADD 主题、脚手架、控件与读数 |

预览示例位于 `com.lcars.ui.preview`，业务应用不应依赖该包。

## 主题

使用三个基于参考 HTML/CSS 的预设之一：

```kotlin
import com.lcars.ui.theme.LcarsPreset
import com.lcars.ui.theme.LcarsTheme

LcarsTheme(spec = LcarsPreset.NemesisBlueUltra.spec) {
    AppContent()
}
```

- `LcarsPreset.ClassicUltra`
- `LcarsPreset.NemesisBlueUltra`
- `LcarsPreset.LowerDecksPadd`

通过 `LcarsTheme.colorScheme`、`typography`、`dimensions`、`shapes` 和 `motionScheme` 读取语义 token。宿主应用可传入 `LcarsMotionMode.Reduced` 或 `Off`。声音通过 `LcarsSoundPlayer` 显式注入，默认保持静音。

## 示例

```kotlin
import com.lcars.ui.display.LcarsAlertBanner
import com.lcars.ui.display.LcarsProgressBar
import com.lcars.ui.layout.LcarsFramePanel

LcarsFramePanel(title = "dynamic states") {
    LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    LcarsAlertBanner(message = "critical alert active", active = true)
}
```

手持布局使用 `com.lcars.ui.padd` 中的 `LcarsPhonePaddTheme(preset = LcarsPreset.LowerDecksPadd)` 与 `LcarsPhonePaddScaffold`。

## 文档

- [中文组件说明](COMPONENTS.zh-CN.md)
- [English component guide](COMPONENTS.en.md)
- [LCARS 24.2 CSS 到 Compose 映射](REFERENCE_MAPPING.md)

