[根目录](../CLAUDE.md) > **automator**

# automator 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

UI 自动化底层库，封装 Android 无障碍服务（AccessibilityService），提供控件查找、遍历、操作、手势执行等核心能力。是 autojs 模块的底层依赖。

---

## 入口与启动

| 文件 | 作用 |
|------|------|
| `src/main/java/com/stardust/view/accessibility/AccessibilityService.kt` | 无障碍服务基类 |
| `src/main/java/com/stardust/automator/GlobalActionAutomator.kt` | 全局手势操作（点击、滑动、返回等） |
| `src/main/java/com/stardust/view/accessibility/LayoutInspector.kt` | 布局检查器 |

---

## 对外接口

### 控件模型

| 类 | 功能 |
|----|------|
| `UiObject.kt` | UI 控件封装 |
| `UiObjectCollection.kt` | 控件集合 |
| `UiGlobalSelector.kt` | 全局选择器 |
| `NodeInfo.kt` | 节点信息 |

### 选择器过滤器（filter/）

| 过滤器 | 功能 |
|--------|------|
| `TextFilters.kt` | 文本匹配 |
| `DescFilters.kt` | 描述匹配 |
| `IdFilter.kt` | ID 匹配 |
| `ClassNameFilters.kt` | 类名匹配 |
| `BoundsFilter.kt` | 边界匹配 |
| `PackageNameFilter.kt` | 包名匹配 |
| `StringContainsFilter.kt` | 包含匹配 |
| `StringMatchesFilter.kt` | 正则匹配 |

### 搜索算法（search/）

| 类 | 算法 |
|----|------|
| `BFS.kt` | 广度优先搜索 |
| `DFS.kt` | 深度优先搜索 |

### 操作（simple_action/）

| 类 | 功能 |
|----|------|
| `SimpleAction.kt` | 简单操作基类 |
| `ScrollAction.kt` | 滚动操作 |
| `FilterAction.kt` | 过滤操作 |
| `SearchTargetAction.kt` | 搜索目标操作 |
| `ActionFactory.kt` | 操作工厂 |

### 全局操作

- `GlobalActionAutomator.kt` -- back、home、recents、notifications、powerDialog、手势执行

---

## 关键依赖与配置

- 无第三方库依赖
- 依赖 `common` 模块

---

## 测试与质量

无测试文件。

---

## 相关文件清单

- `automator/build.gradle.kts` -- 构建配置
- `automator/src/main/java/com/stardust/automator/` -- 控件与操作
- `automator/src/main/java/com/stardust/view/accessibility/` -- 无障碍服务
- `automator/src/main/java/com/stardust/notification/` -- 通知监听

---

## 无障碍服务详解

### AccessibilityService (`view/accessibility/AccessibilityService.kt`)
- **职责**：无障碍服务基类，接收系统无障碍事件并分发给 delegate
- **继承**：`android.accessibilityservice.AccessibilityService`
- **关键字段**：
  - `onKeyObserver` — 按键监听器观察者
  - `keyInterrupterObserver` — 按键拦截器观察者
  - `gestureEventDispatcher` — 手势事件分发器
  - `instance` — 服务单例（companion object）
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `onAccessibilityEvent(event)` | 接收无障碍事件，分发给所有 delegate |
| `onKeyEvent(event)` | 按键事件处理，分发给 observer |
| `onGesture(gestureId)` | 手势事件处理 |
| `fastRootInActiveWindow()` | 快速获取缓存的根节点 |
| `addDelegate(priority, delegate)` | 添加事件处理代理（静态，按优先级排序） |
| `disable()` | 禁用服务（API 24+） |
| `waitForEnabled(timeOut)` | 等待服务启用（支持超时） |

### GlobalActionAutomator (`automator/GlobalActionAutomator.kt`)
- **职责**：全局手势和操作执行器
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `back()` / `home()` / `recents()` | 系统导航操作 |
| `powerDialog()` / `notifications()` / `quickSettings()` | 系统面板操作 |
| `splitScreen()` | 分屏（API 24+） |
| `takeScreenshot()` | 截屏（API 28+） |
| `lockScreen()` | 锁屏（API 28+） |
| `dismissNotificationShade()` | 关闭通知栏（API 31+） |
| `click(x, y)` | 点击坐标 |
| `longClick(x, y)` | 长按坐标 |
| `press(x, y, delay)` | 按压 |
| `swipe(x1, y1, x2, y2, delay)` | 滑动 |
| `gesture(start, duration, points...)` | 单指手势 |
| `gestures(strokes...)` | 多指手势（阻塞） |
| `gesturesAsync(strokes...)` | 多指手势（异步） |
| `setScreenMetrics(metrics)` | 设置屏幕尺寸映射 |

### UiObject (`automator/UiObject.kt`)
- **职责**：UI 控件封装，提供控件操作和属性访问
- **继承**：`AccessibilityNodeInfoCompat`
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `parent()` / `child(i)` / `children()` | 父子节点遍历 |
| `find(selector)` / `findOne(selector)` | 子节点搜索 |
| `text()` / `desc()` / `id()` / `className()` / `packageName()` | 属性获取 |
| `bounds()` / `boundsInParent()` | 边界矩形 |
| `depth()` / `drawingOrder()` / `indexInParent()` | 层级信息 |
| `click()` / `longClick()` | 点击操作 |
| `setText(text)` | 设置文本 |
| `scrollForward()` / `scrollBackward()` / `scrollUp/Down/Left/Right()` | 滚动操作 |
| `setSelection(start, end)` | 设置文本选区 |
| `setProgress(value)` | 设置进度条 |
| `scrollTo(row, column)` | 滚动到指定位置 |
| `copy()` / `paste()` / `cut()` / `select()` | 剪贴板操作 |
| `focus()` / `clearFocus()` | 焦点操作 |
| `collapse()` / `expand()` / `dismiss()` | 展开/折叠/关闭 |
| `checkable()` / `checked()` / `enabled()` / `visibleToUser()` | 状态查询 |
| `clickable()` / `longClickable()` / `scrollable()` / `focusable()` | 能力查询 |
| `row()` / `column()` / `rowSpan()` / `columnSpan()` / `rowCount()` / `columnCount()` | 网格信息 |
| `createRoot(root)` | 从 AccessibilityNodeInfo 创建（静态工厂） |

### UiGlobalSelector (`automator/UiGlobalSelector.kt`)
- **职责**：控件选择器，链式 API 筛选 UI 节点
- **继承**：无
- **筛选方法（第一类：基于属性）**：

| 方法 | 说明 |
|------|------|
| `id(id)` / `idContains()` / `idStartsWith()` / `idEndsWith()` / `idMatches()` | ID 筛选 |
| `text(text)` / `textContains()` / `textStartsWith()` / `textEndsWith()` / `textMatches()` | 文本筛选 |
| `desc(desc)` / `descContains()` / `descStartsWith()` / `descEndsWith()` / `descMatches()` | 描述筛选 |
| `className(name)` / `classNameContains()` / `classNameStartsWith()` / `classNameMatches()` | 类名筛选 |
| `packageName(name)` / `packageNameContains()` / `packageNameStartsWith()` / `packageNameMatches()` | 包名筛选 |
| `bounds(l, t, r, b)` / `boundsInside()` / `boundsContains()` | 边界筛选 |
| `drawingOrder(order)` | 绘制顺序 |

- **第二类筛选（布尔属性）**：`clickable()`, `longClickable()`, `checkable()`, `checked()`, `scrollable()`, `enabled()`, `focusable()`, `focused()`, `selected()`, `editable()`, `password()`, `depth(depth)`

- **搜索算法**：`findOnce()`, `find()`, `findOne()`, `untilFind()` — 支持 BFS/DFS 切换

### NodeInfo (`view/accessibility/NodeInfo.kt`)
- **职责**：节点信息快照，用于布局检查器和调试
- **继承**：无（数据类，带 `@Keep` 注解）
- **关键属性**：`id`, `fullId`, `desc`, `className`, `packageName`, `text`, `depth`, `boundsInScreen`, `boundsInParent`, 以及所有布尔状态（clickable, checked, scrollable 等）
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `capture(context, root)` | 从根节点递归捕获整棵节点树（静态） |
| `getChildren()` | 获取子节点列表 |

### LayoutInspector (`view/accessibility/LayoutInspector.kt`)
- **职责**：布局检查器，捕获当前窗口的控件树
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `captureCurrentWindow()` | 捕获当前窗口（异步执行） |
| `addCaptureAvailableListener(l)` | 添加捕获完成监听 |
| `clearCapture()` | 清除缓存 |
| `capture` | 最新捕获结果（NodeInfo） |
| `isDumping` | 是否正在 dump |

### 过滤器体系 (`automator/filter/`)

| 类 | 说明 |
|----|------|
| `Filter` | 过滤器接口基类 |
| `BooleanFilter` | 布尔属性过滤器 |
| `IntFilter` | 整数属性过滤器 |
| `KeyGetter` | 键值获取接口 |
| `TextFilters` | 文本过滤（equals/contains/startsWith/endsWith/matches） |
| `DescFilters` | 描述过滤 |
| `IdFilter` | ID 过滤 |
| `ClassNameFilters` | 类名过滤 |
| `PackageNameFilter` | 包名过滤 |
| `BoundsFilter` | 边界过滤 |
| `StringContainsFilter` / `StringEqualsFilter` / `StringStartsWithFilter` / `StringEndsWithFilter` / `StringMatchesFilter` | 字符串匹配过滤器 |
| `Selector` | 过滤器容器，组合多个 Filter |

### 搜索算法 (`automator/search/`)

| 类 | 说明 |
|----|------|
| `SearchAlgorithm` | 搜索算法接口 |
| `BFS` | 广度优先搜索 |
| `DFS` | 深度优先搜索（默认） |

### 操作体系 (`automator/simple_action/`)

| 类 | 说明 |
|----|------|
| `SimpleAction` | 简单操作基类 |
| `ActionTarget` | 操作目标接口 |
| `ActionFactory` | 操作工厂 |
| `FilterAction` | 过滤操作 |
| `ScrollAction` | 滚动操作 |
| `ScrollMaxAction` | 最大滚动操作 |
| `SearchTargetAction` | 搜索目标操作 |
| `SearchUpTargetAction` | 向上搜索目标操作 |
| `DepthFirstSearchTargetAction` | 深度优先搜索目标操作 |
| `Able` | 能力接口（clickable, scrollable 等） |

### 通知监听 (`notification/`)

| 类 | 说明 |
|----|------|
| `Notification` | 通知数据模型 |
| `NotificationListenerService` | 通知监听服务 |
