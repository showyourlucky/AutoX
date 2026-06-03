[根目录](../CLAUDE.md) > **app**

# app 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

主应用程序模块，是 AutoX.js 的 Android App 入口。负责 UI 界面展示、脚本管理与执行、APK 构建与签名、VS Code 远程调试连接（DevPlugin）、定时任务调度、主题管理等。

---

## 入口与启动

| 文件 | 作用 |
|------|------|
| `src/main/java/org/autojs/autojs/App.kt` | Application 类，全局初始化入口 |
| `src/main/java/org/autojs/autojs/ui/splash/SplashActivity.kt` | 启动页 |
| `src/main/java/org/autojs/autojs/ui/main/MainActivity.kt` | 主界面（Compose + ViewPager2） |
| `src/main/AndroidManifest.xml` | 应用清单 |

### App.kt 初始化流程

1. `GlobalAppContext.set()` -- 设置全局上下文
2. `AutoJs.initInstance()` -- 初始化脚本引擎单例
3. `GlobalKeyObserver.init()` -- 音量键控制（可选）
4. `setupDrawableImageLoader()` -- Glide 图片加载器
5. `TimedTaskScheduler.init()` -- 定时任务调度器
6. `initDynamicBroadcastReceivers()` -- 动态广播接收器

---

## 对外接口

### UI 界面

| Activity/Fragment | 功能 |
|-------------------|------|
| `MainActivity.kt` | 主页：脚本列表 + 任务管理 + WebView 文档 |
| `build/BuildActivity.kt` | APK 构建界面 |
| `build/ProjectConfigActivity.kt` | 项目配置 |
| `build/SignManageActivityKt.kt` | 签名管理 |
| `log/LogActivityKt.kt` | 日志查看器 |
| `edit/` | 脚本编辑器（代码高亮、自动补全、调试） |
| `floating/` | 悬浮窗：圆形菜单、布局检查器 |

### DevPlugin 远程连接

- `devplugin/DevPlugin.kt` -- WebSocket 客户端/服务端，支持 VS Code 插件远程调试
- 端口：9317
- 协议：WebSocket JSON + Binary

### APK 构建

- `build/ApkBuilder.kt` -- APK 构建封装
- `build/ApkSigner.kt` / `build/ApkKeyStore.kt` -- 签名管理
- `build/DefaultSign.kt` -- 默认签名配置
- `build/apksigner/` -- APK 签名算法实现（ZipSigner 等）

### 定时任务

- `timing/TimedTask.kt` -- 定时任务数据模型
- `timing/TimedTaskManager.kt` -- 任务管理器
- `timing/TimedTaskScheduler.kt` -- 调度器
- `timing/work/` -- WorkManager / AlarmManager / AndroidJob 提供者

---

## 关键依赖与配置

- **Jetpack Compose**：UI 层（Compose 1.2.0-rc01）
- **Accompanist**：系统 UI 控制、权限、分页等 Compose 扩展
- **Ktor**：WebSocket 通信（DevPlugin）
- **Bugly**：崩溃上报（appId: 19b3607b53）
- **Flurry**：数据统计
- **Glide**：图片加载
- **Retrofit + RxJava**：网络请求
- **LeakCanary**：内存泄漏检测（Debug 模式）

### build.gradle.kts

- applicationId: `org.autojs.autoxjs`
- Flavor `v6` 后缀: `.v6`

---

## 数据模型

- `storage/database/TimedTaskDatabase.kt` -- 定时任务数据库
- `storage/database/IntentTaskDatabase.kt` -- Intent 任务数据库
- `network/entity/` -- 网络 API 实体（User、Topic、Config、Notification 等）

---

## 测试与质量

- 测试文件：`src/test/java/org/autojs/autojs/ExampleUnitTest.java`（占位）、`Test.kt`
- 无集成测试
- LeakCanary 集成于 Debug 构建

---

## 常见问题 (FAQ)

**Q: 编译报错找不到 template.apk？**
A: 需要先编译 inrt 模块的 template 变体，执行 `./gradlew inrt:assembleTemplateDebug && ./gradlew inrt:cp2APPDebug`

**Q: 签名文件找不到？**
A: 签名配置位于项目根目录 `keystores/sign.properties`，路径已改为相对路径，无需再手动配置

---

## 相关文件清单

- `app/build.gradle.kts` -- 构建配置
- `app/proguard-rules.pro` -- 混淆规则
- `app/src/main/java/org/autojs/autojs/` -- 主源码目录
- `app/src/main/res/` -- 资源文件
- `app/src/main/res-i18n/` -- 国际化资源

---

## UI 层详解

### 编辑器 (`ui/edit/`)

#### 核心类

| 类名 | 类型 | 说明 |
|------|------|------|
| `EditActivity.java` | Activity | 脚本编辑器入口，接收 path/content/name/readOnly 参数 |
| `EditorView.java` | View | 编辑器主视图，组合 CodeEditor + Toolbar + DebugBar |
| `EditorMenu.java` | Menu | 编辑器菜单（运行、保存、格式化、搜索等） |
| `FindOrReplaceDialogBuilder.java` | Dialog | 查找/替换对话框 |
| `ClassSearchDialogBuilder.java` | Dialog | Java 类搜索对话框 |
| `TextSizeSettingDialogBuilder.java` | Dialog | 字体大小设置 |
| `ViewSampleActivity.java` | Activity | 代码示例查看 |

#### 编辑器组件 (`ui/edit/editor/`)

| 类名 | 类型 | 说明 |
|------|------|------|
| `CodeEditor.java` | View | 代码编辑器（继承 HVScrollView），支持语法高亮、查找替换、格式化 |
| `CodeEditText.java` | EditText | 代码文本输入框，行号、光标、语法高亮绘制 |
| `HVScrollView.java` | ScrollView | 水平+垂直滚动容器 |
| `LayoutHelper.java` | Helper | 布局计算辅助 |
| `JavaScriptHighlighter.java` | Highlighter | JavaScript 语法高亮器 |
| `TextViewUndoRedo.java` | Helper | 撤销/重做管理器 |
| `AutoIndent.java` | Helper | 自动缩进 |
| `BracketMatching.java` | Helper | 括号匹配 |

#### 调试 (`ui/edit/debug/`)

| 类名 | 类型 | 说明 |
|------|------|------|
| `DebugBar.java` | View | 调试工具栏 |
| `CodeEvaluateDialogBuilder.java` | Dialog | 代码求值对话框 |
| `CodeEvaluator.java` | Helper | 代码求值器 |
| `DebuggerSingleton.java` | Singleton | 调试器单例管理 |
| `WatchingVariable.java` | Model | 监视变量模型 |

#### 工具栏 (`ui/edit/toolbar/`)

| 类名 | 类型 | 说明 |
|------|------|------|
| `ToolbarFragment.java` | Fragment | 工具栏基类 |
| `NormalToolbarFragment.java` | Fragment | 普通模式工具栏（运行、保存、更多） |
| `DebugToolbarFragment.java` | Fragment | 调试模式工具栏（继续、单步、停止） |
| `SearchToolbarFragment.java` | Fragment | 搜索工具栏 |

#### 主题与键盘 (`ui/edit/theme/`, `ui/edit/keyboard/`)

| 类名 | 说明 |
|------|------|
| `Theme.java` | 编辑器主题模型 |
| `Themes.kt` | 主题管理与切换 |
| `TokenMapping.java` | 语法 Token 到颜色的映射 |
| `FunctionsKeyboardHelper.java` | 函数键盘辅助 |
| `FunctionsKeyboardView.java` | 函数键盘视图 |

---

### APK 构建 (`ui/build/`)

| 类名 | 类型 | 说明 |
|------|------|------|
| `BuildActivity.kt` | Activity (Compose) | APK 构建界面，使用 Jetpack Compose |
| `BuildPage.kt` | Composable | 构建页面 UI 组件 |
| `BuildViewModel.kt` | ViewModel | 构建逻辑（签名选择、APK 打包、版本配置） |
| `ProjectConfigActivity.kt` | Activity | 项目配置编辑（包名、版本、图标等） |
| `SignManageActivityKt.kt` | Activity | 签名管理界面 |
| `SignManageViewModel.kt` | ViewModel | 签名管理逻辑 |
| `SignKeyCreateDialogBuilder.java` | Dialog | 创建签名密钥对话框 |

---

### 悬浮窗 (`ui/floating/`)

| 类名 | 类型 | 说明 |
|------|------|------|
| `CircularMenu.kt` | View | 圆形悬浮菜单（录制、布局检查、运行脚本、设置） |
| `CircularMenuWindow.java` | Window | 悬浮窗窗口管理 |
| `CircularMenuFloaty.java` | Floaty | 悬浮窗适配 |
| `CircularActionMenu.java` | View | 圆形动作菜单 |
| `FloatyWindowManger.java` | Manager | 悬浮窗全局管理 |
| `FullScreenFloatyWindow.java` | Window | 全屏悬浮窗 |
| `MyLifecycleOwner.kt` | LifecycleOwner | Compose 生命周期持有者 |
| `OrientationAwareWindowBridge.java` | Bridge | 屏幕方向感知窗口桥接 |

#### 手势 (`ui/floating/gesture/`)

| 类名 | 说明 |
|------|------|
| `DragGesture.java` | 拖拽手势 |
| `BounceDragGesture.java` | 弹性拖拽手势 |

#### 布局检查器 (`ui/floating/layoutinspector/`)

| 类名 | 说明 |
|------|------|
| `LayoutBoundsFloatyWindow.kt` | 布局边界悬浮窗 |
| `LayoutBoundsView.java` | 布局边界绘制视图 |
| `LayoutHierarchyFloatyWindow.kt` | 布局层级悬浮窗 |
| `OnNodeInfoSelectListener.java` | 节点选中回调 |

---

### 文件浏览器 (`ui/explorer/`)

| 类名 | 类型 | 说明 |
|------|------|------|
| `ExplorerViewKt.kt` | View | 文件浏览器主视图（列表/网格、排序、过滤） |
| `ExplorerView.java` | View | 文件浏览器（Java 版本） |
| `ExplorerViewHelper.java` | Helper | 浏览器辅助工具 |
| `ExplorerProjectToolbar.kt` | View | 项目工具栏 |

---

### 文件选择器 (`ui/filechooser/`)

| 类名 | 说明 |
|------|------|
| `FileChooseListView.kt` | 文件选择列表视图 |
| `FileChooserDialogBuilder.java` | 文件选择对话框构建器 |

---

### 通用对话框 (`ui/common/`)

| 类名 | 说明 |
|------|------|
| `FileNameInputDialog.java` | 文件名输入对话框 |
| `NotAskAgainDialog.java` | "不再询问"对话框 |
| `OperationDialogBuilder.java` | 操作选择对话框 |
| `OptionListView.java` | 选项列表视图 |
| `ProgressDialog.java` | 进度对话框 |
| `RxDialogs.java` | RxJava 对话框工具 |
| `ScriptLoopDialog.java` | 脚本循环执行对话框 |
| `ScriptOperations.java` | 脚本操作集合（运行、编辑、删除、重命名、发送） |

---

### 自定义控件 (`ui/widget/`)

| 类名 | 说明 |
|------|------|
| `AutoAdapter.java` | 自动适配器 |
| `BackgroundTarget.kt` | Glide 图片目标（背景） |
| `BindableViewHolder.java` | 绑定型 ViewHolder |
| `CheckBoxCompat.java` | 兼容 CheckBox |
| `GridDividerDecoration.java` | 网格分割线装饰 |
| `ItemTouchHelperSimpleCallback.java` | 拖拽排序回调 |
| `OnItemClickListener.java` | 点击监听接口 |
| `ScrollAwareFABBehavior.java` | FAB 滚动感知行为 |
| `SearchViewItem.java` | 搜索视图项 |
| `SimpleAdapterDataObserver.java` | 数据变化观察者 |
| `SimpleRecyclerViewAdapter.java` | 简单 RecyclerView 适配器 |
| `SimpleTextWatcher.java` | 文本变化监听 |
| `SwitchCompat.java` | 兼容 Switch |
| `ViewHolderMutableAdapter.java` | 可变 ViewHolder 适配器 |
| `ViewHolderSupplier.java` | ViewHolder 供应接口 |
| `CallbackBundle.java` | 回调 Bundle |
| `DownloadManagerUtil.java` | 下载管理工具 |
| `DownloadReceiver.java` | 下载广播接收器 |

---

### Compose 组件 (`ui/compose/`)

#### 主题 (`ui/compose/theme/`)

| 类名 | 说明 |
|------|------|
| `Color.kt` | 颜色定义 |
| `Shape.kt` | 形状定义 |
| `Theme.kt` | AutoXJsTheme 主题 |
| `Type.kt` | 字体定义 |

#### 工具 (`ui/compose/util/`)

| 类名 | 说明 |
|------|------|
| `SetSystemUI.kt` | 系统 UI 设置（状态栏、导航栏颜色） |

#### 控件 (`ui/compose/widget/`)

| 类名 | 说明 |
|------|------|
| `Dialog.kt` | Compose 对话框 |
| `Icon.kt` | 图标组件 |
| `SearchBox.kt` | 搜索框 |
| `SwipeRefresh.kt` | 下拉刷新 |
| `Switch.kt` | 开关组件 |

---

### 其他 UI 模块

| 子目录 | 类名 | 说明 |
|--------|------|------|
| `ui/doc/` | `ManualDialog.java` | API 手册对话框 |
| `ui/error/` | `AbstractIssueReporterActivity.java` | 错误报告基类 |
| `ui/error/` | `ErrorReportActivity.java` | 错误报告界面 |
| `ui/error/` | `IssueReporterActivity.java` | Issue 报告（GitHub） |
| `ui/settings/` | `TestSurfaceView.java` | Surface 测试视图 |
| `ui/codegeneration/` | `CodeGenerateDialog.java` | 代码生成对话框 |
