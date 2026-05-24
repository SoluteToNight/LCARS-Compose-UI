# LCARS Compose 组件说明

本文档说明 `:lcars-ui` 模块中的可复用 Jetpack Compose 组件。组件遵循 LCARS 视觉规则：黑色底面、纯色几何块、紧凑间距、大圆角/单侧圆角结构、上屏文本大写、告警使用阶跃动画。

英文版本见 [COMPONENTS.en.md](COMPONENTS.en.md)。

## 使用方式

所有组件都应放在 `LcarsTheme` 下使用。主题提供 LCARS 颜色、字体和间距 token。

```kotlin
LcarsTheme(style = LcarsStyle.ClassicUltra) {
    LcarsFramePanel(title = "sensor deck") {
        LcarsStatusLight(label = "sensor lock", active = true)
        LcarsProgressBar(progress = 0.64f, label = "reactor balance")
    }
}
```

## 主题与基础 Token

### `LcarsTheme`

功能：为组件树提供 LCARS 专用的颜色、字体和间距，不依赖动态 Material 颜色。

主要内容：

- `LcarsStyle`：预设风格 token 集。当前包括 `ClassicUltra`、`LowerDecks`、`LowerDecksPadd` 和 `NemesisBlueUltra`。
- `LcarsColors`：LCARS 色板，包括琥珀色、浅蓝、紫色、警报红、战术绿、黑色背景等。
- `LcarsTypography`：标题、按钮、遥测、短标签文字样式。
- `LcarsSpacing`：标准间距、按钮最小尺寸、条形高度、elbow 厚度等。

适用场景：应用根节点、预览、独立组件示例。

套用风格：

```kotlin
LcarsTheme(style = LcarsStyle.LowerDecks) {
    AppContent()
}
```

显式传入的 token 会覆盖风格默认值：

```kotlin
LcarsTheme(
    style = LcarsStyle.NemesisBlueUltra,
    colors = customColors,
    spacing = customSpacing,
) {
    AppContent()
}
```

### `LcarsAdaptiveTheme`

功能：在 `LcarsTheme` 之上增加横竖屏/紧凑横屏自适应 token。它根据容器宽高解析 `LcarsResponsiveMode`，并提供 `LocalLcarsAdaptiveProfile`。

行为约定：

- 宽横屏保持默认尺寸。
- 竖屏和紧凑横屏压缩几何尺寸、间距、按钮高度、命令轨宽度等。
- 字体保持默认一倍逻辑分辨率和原有 `sp` 大小；自适应主题不额外放大、缩小或重采样字体。

适用场景：应用根节点、demo 根节点、需要横竖屏自动切换尺寸 token 的页面。

`LcarsAdaptiveTheme` 接受和 `LcarsTheme` 相同的 `style`、`colors`、`typography`、`spacing` 参数；自适应尺寸会在风格间距解析后再应用。

## 基础几何组件

### `LcarsButton`

功能：LCARS 命令按钮，支持药丸形、左侧圆角、右侧圆角和矩形形态。按钮文字自动大写，默认贴近右下角。

主要参数：

- `text`：按钮标签。
- `onClick`：点击回调。
- `color` / `contentColor`：背景色和文字色。
- `shape`：`Pill`、`BlockStart`、`BlockEnd`、`Rectangle`。
- `alerting`：启用阶跃式警报闪烁。
- `enabled`：禁用时按 LCARS 参考行为隐藏。

适用场景：命令入口、模式切换、确认操作、侧边控制组。

### `LcarsBar`

功能：水平 LCARS 条形块，可带左右圆角端帽和嵌入式标签。

主要参数：

- `color` / `height`：条形颜色和高度。
- `startCap` / `endCap`：是否显示圆角端帽。
- `label`：嵌入条形中的文字。
- `labelAlign`：标签左、中、右对齐。

适用场景：页面标题条、分区标题、状态页脚、框架边线。

### `LcarsElbow`

功能：绘制 LCARS 典型的 L 形大圆角转角结构。

主要参数：

- `direction`：`TopLeft`、`TopRight`、`BottomLeft`、`BottomRight`。
- `wingWidth` / `wingHeight`：整体尺寸。
- `thickness`：几何块厚度。
- `text`：角块中的短标签。

适用场景：PADD 顶部/底部角、控制台侧翼、页面视觉锚点。

### `LcarsFramePanel`

功能：带 LCARS 标题条和可选页脚条的内容面板。

主要参数：

- `title`：面板标题。
- `footerLabel`：可选页脚标签。
- `content`：面板内容 slot。

适用场景：组件 catalog 分区、数据面板、状态面板。

## 控制类组件

### `LcarsCommandRail`

功能：标准 LCARS 命令轨，组合命令按钮、被动块、占位块和警报块。

主要参数：

- `items`：`LcarsCommandRailItem` 列表。
- `side`：左侧或右侧轨道，影响单侧圆角方向。
- `compact`：启用紧凑尺寸。
- `onCommandClick`：命令项点击回调。

`LcarsCommandRailItemType`：

- `Command`：可点击命令。
- `PassiveBlock`：只显示信息的几何块。
- `SpacerBlock`：填充高度的纯色块。
- `AlertBlock`：带阶跃警报闪烁的命令块。

适用场景：主控台左/右命令栏、PADD 快捷命令组、固定导航轨。

### `LcarsSegmentedControl`

功能：LCARS 模式选择器，用多个几何块表示互斥选项。

主要参数：

- `options`：选项文本列表。
- `selectedOption`：当前选中项。
- `onOptionSelected`：选择回调。
- `enabledOptions`：可用选项集合。
- `alerting`：选中项警报闪烁。

适用场景：`NAV / COMM / SENSOR`、视图模式、工作流状态切换。

### `LcarsToggle`

功能：二元状态控制，外观是 LCARS 几何块，不是系统 Switch。

主要参数：

- `checked`：当前状态。
- `onCheckedChange`：状态变更回调。
- `checkedLabel` / `uncheckedLabel`：两侧状态文案。
- `alerting`：启用警报闪烁。

适用场景：在线/待机、武装/解除、锁定/解锁、授权/未授权。

### `LcarsDialog`

功能：LCARS 风格确认/警告/授权弹窗。使用 Android dialog 容器能力，但视觉不使用 Material 外观。

主要参数：

- `title` / `message`：标题和正文。
- `confirmLabel` / `dismissLabel`：确认和取消按钮。
- `level`：`Normal`、`Advisory`、`Warning`、`Critical`。
- `onConfirm` / `onDismiss`：操作回调。

适用场景：危险操作确认、授权请求、警告提示。

## 数据与状态显示

### `LcarsTelemetryPanel`

功能：响应式遥测读数网格，显示标签、值和状态颜色。

主要参数：

- `title`：面板标题。
- `entries`：`LcarsTelemetryEntry` 列表。
- `alerting`：在告警态改变正常状态项的显示。
- `singleColumnBelow`：低于指定宽度时切换单列。

适用场景：坐标、传感器状态、系统指标、设备状态。

### `LcarsDataTable`

功能：紧凑状态表格，支持表头和高亮行。

主要参数：

- `headers`：表头文本。
- `rows`：`LcarsDataRow` 列表。

适用场景：子系统状态、诊断代码、任务列表。

### `LcarsLogConsole`

功能：日志/事件流面板，按严重级别着色，固定显示 `maxLines` 条，避免在滚动页面中嵌套滚动容器。

主要参数：

- `entries`：`LcarsLogEntry` 列表。
- `maxLines`：显示行数。
- `compact`：紧凑内边距。
- `autoScroll`：为 `true` 时显示末尾日志，为 `false` 时显示开头日志。

适用场景：事件记录、诊断输出、通信流、后台任务状态。

### `LcarsNumberMatrix`

功能：稳定可复现的随机数字矩阵，支持运行态扫描高亮。

主要参数：

- `rows` / `columns`：矩阵大小。
- `seed`：随机种子，相同 seed 输出一致。
- `running`：启用高亮行动画。
- `highlightedRow`：手动指定高亮行。

适用场景：LCARS 背景数据、扫描面板、诊断矩阵。

### `LcarsStarCoords`

功能：周期刷新星图坐标读数。

主要参数：

- `count`：坐标行数。
- `digits`：数字位数。
- `updateIntervalMillis`：刷新间隔。
- `running`：是否自动刷新。
- `seed`：初始随机种子。

适用场景：导航星图、扫描屏、坐标叠层。

### `LcarsNumericLabel`

功能：带左右 pill 块的数字标签，模拟 LCARS 参考中的大号数字标识。

主要参数：

- `label`：数字文本。
- `color`：几何块和数字颜色。
- `height`：标签高度。

适用场景：区域编号、面板编号、设备编号。

## 动态视觉组件

### `LcarsAlertBanner`

功能：警报横幅。`Critical` 级别使用阶跃闪烁。

适用场景：全局告警、状态摘要、危险提示。

### `LcarsStatusLight`

功能：状态灯加文字标签，支持启用/禁用、警报闪烁。

适用场景：传感器锁定、连接状态、子系统在线状态。

### `LcarsProgressBar`

功能：LCARS 纯色进度条，可显示标签和百分比。

适用场景：能量平衡、任务进度、载入状态。

### `LcarsSegmentedMeter`

功能：由多个矩形段组成的仪表。

适用场景：功率等级、信号强度、容量读数。

### `LcarsScannerSweep`

功能：水平扫描线和网格背景。

适用场景：扫描区、雷达/传感器读数、动态背景。

### `LcarsTargetScanner`

功能：扩张式目标选择框，模拟扫描/锁定过程。

主要参数：

- `running`：是否播放扫描动画。
- `color`：括号颜色。
- `scanDurationMillis`：扫描周期。

适用场景：目标锁定、传感器搜索、星图叠层。

### `LcarsReadoutTicker`

功能：周期切换的单行读数。

适用场景：状态广播、轮询消息、滚动遥测摘要。

## 布局与框架组件

### `LcarsAppScaffold`

功能：通用应用外壳，包含顶部标题条、左侧控制列、内容区和可选页脚。

适用场景：桌面/平板尺寸的基础 LCARS 应用页面。

### `LcarsPaddScaffold`

功能：PADD 风格页面外壳，适合竖屏和窄屏。

适用场景：手机竖屏、手持设备界面、单页工具。

### `LcarsConsoleScaffold` / `LcarsMainConsole`

功能：宽屏主控台布局，包含左侧翼和主甲板内容区。

适用场景：横屏控制台、演示主屏、操作台界面。

### `LcarsResponsiveScaffold`

功能：根据尺寸选择三个布局 slot：竖屏、紧凑横屏、宽横屏。

主要参数：

- `portrait`：竖屏/窄屏内容。
- `compactLandscape`：低高度横屏内容。
- `wideLandscape`：宽横屏内容。
- `compactWidth` / `compactLandscapeHeight`：断点配置。

适用场景：同一页面需要适配手机竖屏、折叠屏横屏和平板/桌面横屏。

### `LcarsDividerGrid`

功能：多段式 LCARS 分隔网格，比单条 `LcarsBar` 更适合复杂页面结构。

主要参数：

- `type`：`Type1`、`Type2`、`Type3`。
- `topHeight` / `bottomHeight`：上下行高度。

适用场景：内容区分隔、标题区装饰、复杂页面结构线。

### `LcarsInspectBracket`

功能：带侧边刻度、角框和移动标记的检查框架，中间提供内容 slot。

主要参数：

- `color`：主框架颜色。
- `running`：是否播放侧边 marker 动画。
- `content`：被检查对象或扫描内容。

适用场景：目标检查、对象详情、星图 inspection 模式。

## 全屏与高级场景

### `LcarsTransmissionFrame`

功能：通信/授权场景的全屏模板，包含顶部和底部 LCARS 条、中间标题、副标题和内容 slot。

主要参数：

- `headerLabel` / `footerLabel`：顶部和底部标签。
- `title` / `subtitle`：中央标题和说明。
- `content`：可选中心内容。

适用场景：来电通信、授权页面、锁屏式提示、全屏状态页。

### `LcarsStarChart`

功能：Canvas 星图，绘制星点、网格、星名标签、坐标读数和目标扫描叠层。

主要参数：

- `mode`：`Navigation` 或 `Inspection`。
- `stars`：可选自定义星点列表。
- `seed`：默认星点生成种子。
- `showCoords`：是否显示坐标读数。
- `showScanner`：是否显示目标扫描叠层。
- `running`：是否运行内部扫描、坐标刷新和检查框动画；组件目录等长页面可设为 `false` 降低重组开销。

`Inspection` 模式会使用 `LcarsInspectBracket` 包裹星图，适合目标检查页面。

### `LcarsStar`

功能：星图数据模型。

字段：

- `label`：星名。
- `x` / `y`：相对坐标，范围通常为 `0f..1f`。
- `size`：星点大小。
- `labeled`：是否绘制选择框和标签。

## 设计注意事项

- 默认背景应保持纯黑。
- 有色控制块里的文字通常使用黑色。
- 避免阴影、渐变、玻璃、Material surface 风格。
- 在滚动页面中不要嵌套可垂直滚动组件；`LcarsLogConsole` 已设计为固定行显示。
- 动画应偏向阶跃、扫描、闪烁，而不是柔和呼吸效果。
- `seed` 参数用于稳定预览和测试，传入相同 seed 应得到相同数据。
