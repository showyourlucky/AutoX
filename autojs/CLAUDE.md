[根目录](../CLAUDE.md) > **autojs**

# autojs 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

AutoX.js 的核心脚本引擎库。封装 Rhino JavaScript 引擎，提供完整的脚本运行时 API（文件IO、网络、图像处理、OCR、UI、传感器、线程、悬浮窗等），是整个项目最核心和最庞大的模块。

---

## 入口与启动

| 文件 | 作用 |
|------|------|
| `src/main/java/com/stardust/autojs/engine/RhinoJavaScriptEngine.kt` | Rhino JS 引擎封装 |
| `src/main/java/com/stardust/autojs/runtime/ScriptRuntime.java` | 脚本运行时，聚合所有 API |
| `src/main/assets/init.js` | JS 初始化脚本，设置全局函数 |
| `src/main/java/com/stardust/autojs/ScriptEngineServiceBuilder.java` | 引擎服务构建器 |
| `src/main/java/com/stardust/autojs/Config.java` | 配置管理 |

### 引擎初始化流程

1. `RhinoJavaScriptEngine` 创建 Rhino Context（ES6, optimizationLevel=-1）
2. 创建 `TopLevelScope` 并初始化标准对象
3. `initRequireBuilder()` 设置 CommonJS 模块加载器（从 assets/modules 加载）
4. 执行 `init.js` 初始化全局函数（importClass、__asGlobal__ 等）

---

## 对外接口

### ScriptRuntime 暴露的 JS API

| JS 对象 | Java/Kotlin 类 | 功能 |
|---------|----------------|------|
| `app` | `AppUtils` | 应用操作（启动、卸载、打开 URL） |
| `console` | `Console` | 控制台输出 |
| `automator` | `SimpleActionAutomator` | UI 自动化操作（点击、滑动、输入） |
| `ui` | `UI` | UI 布局与控件 |
| `dialogs` | `Dialogs` | 对话框 |
| `events` | `Events` | 事件监听 |
| `files` | `Files` | 文件操作 |
| `http` | 内置模块 | HTTP 请求 |
| `images` | `Images` | 图像处理（截图、找色、找图） |
| `timers` | `Timers` | 定时器 |
| `threads` | `Threads` | 多线程 |
| `engines` | `Engines` | 脚本引擎管理 |
| `floaty` | `Floaty` | 悬浮窗 |
| `sensors` | `Sensors` | 传感器 |
| `media` | `Media` | 媒体操作 |
| `selector` | `UiSelector` | UI 控件选择器 |

### OCR API

| 类 | 功能 |
|----|------|
| `runtime/api/Paddle.kt` | PaddleOCR 文字识别 |
| `runtime/api/GoogleMLKit.kt` | Google ML Kit 文字识别（中/英/日/韩/天城文） |
| `core/mlkit/GoogleMLKitOcrResult.kt` | ML Kit 结果模型 |
| `runtime/api/Images.java` | 图像处理 API |

### JS 模块（assets/modules/）

| 模块文件 | 功能 |
|----------|------|
| `__app__.js` | app 对象 |
| `__automator__.js` | 自动化操作 |
| `__console__.js` | 控制台 |
| `__dialogs__.js` | 对话框 |
| `__events__.js` | 事件 |
| `__files__.js` | 文件操作 |
| `__floaty__.js` | 悬浮窗 |
| `__http__.js` | HTTP |
| `__images__.js` | 图像处理 |
| `__paddle__.js` | PaddleOCR |
| `__selector__.js` | 选择器 |
| `__sensors__.js` | 传感器 |
| `__shell__.js` | Shell 命令 |
| `__threads__.js` | 线程 |
| `__timers__.js` | 定时器 |
| `__ui__.js` | UI |
| `__engines__.js` | 引擎管理 |
| `__storages__.js` | 本地存储 |
| `__web__.js` | WebView |

---

## 关键依赖与配置

- **Rhino 1.7.14**：JavaScript 引擎（ES6，解释模式）
- **OpenCV 4.5.5**：图像处理（通过 LocalRepo/OpenCV）
- **PaddleOCR**：中文 OCR（通过 paddleocr 模块）
- **Google ML Kit**：多语言 OCR
- **Tesseract4Android**：传统 OCR 备选
- **EventBus**：事件总线
- **OkHttp**：HTTP 客户端
- **RootShell**：Root 命令执行
- **EnhancedFloaty**：悬浮窗框架
- **zip4j** / **p7zip**：压缩处理

---

## 数据模型

- `core/database/Database.java` -- SQLite 数据库操作
- `core/storage/LocalStorage.java` -- 本地键值存储
- `project/ProjectConfig.kt` -- 项目配置
- `script/` -- 脚本源模型（JavaScriptSource、AutoFileSource 等）

---

## 测试与质量

| 测试文件 | 状态 |
|----------|------|
| `MultiLinePreprocessorTest.java` | 多行预处理器测试 |
| `XmlConverterTest.java` | XML 转换器测试 |
| `ExampleUnitTest.java` | 占位 |
| `Test.kt` | 占位 |

---

## 常见问题 (FAQ)

**Q: Rhino 的 optimizationLevel 为什么是 -1？**
A: Android 不支持 JVM 字节码生成，必须使用解释模式。这会影响性能但保证兼容性。

**Q: 如何添加新的 JS API？**
A: 1) 在 `runtime/api/` 创建 Java/Kotlin 类；2) 在 `ScriptRuntime.java` 中添加 `@ScriptVariable` 字段；3) 在 `assets/modules/` 创建对应的 JS 模块。

---

## 相关文件清单

- `autojs/build.gradle.kts` -- 构建配置
- `autojs/src/main/java/com/stardust/autojs/` -- 核心源码
- `autojs/src/main/assets/modules/` -- JS 模块
- `autojs/src/main/assets/init.js` -- 初始化脚本
- `autojs/src/main/res-i18n/` -- 国际化资源

---

## Runtime API 详解

### Images
- **职责**：图像处理 API（截图、找色、找图、模板匹配、图像变换）
- **继承**：无（普通类）
- **关键字段**：`colorFinder` (ColorFinder) — 颜色查找器
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `requestScreenCapture(int orientation)` | 请求屏幕截图权限，返回 PromiseAdapter |
| `captureScreen()` | 截取当前屏幕，返回 ImageWrapper |
| `captureScreen(String path)` | 截屏并保存到指定路径 |
| `read(String path)` | 从文件读取图片 |
| `load(String src)` | 从 URL 加载图片 |
| `save(ImageWrapper, String path, String format, int quality)` | 保存图片到文件 |
| `copy(ImageWrapper)` | 克隆图片 |
| `clip(ImageWrapper, int x, int y, int w, int h)` | 裁剪图片 |
| `rotate(ImageWrapper, float x, float y, float degree)` | 旋转图片 |
| `concat(ImageWrapper, ImageWrapper, int direction)` | 拼接两张图片（LEFT/RIGHT/TOP/BOTTOM） |
| `pixel(ImageWrapper, int x, int y)` | 获取像素颜色值 |
| `findImage(ImageWrapper, ImageWrapper, float threshold, Rect rect)` | 模板匹配找图，返回 Point |
| `matchTemplate(ImageWrapper, ImageWrapper, ...)` | 多目标模板匹配，返回 Match 列表 |
| `fromBase64(String)` / `toBase64(ImageWrapper, String, int)` | Base64 编解码 |
| `fromBytes(byte[])` / `toBytes(ImageWrapper, String, int)` | 字节数组转换 |
| `newMat()` / `newMat(Mat, Rect)` | 创建 OpenCV Mat |
| `initOpenCvIfNeeded()` | 延迟初始化 OpenCV |

### Events
- **职责**：事件监听系统（按键、触摸、通知、Toast、手势、广播）
- **继承**：`EventEmitter` implements `OnKeyListener`, `TouchObserver.OnTouchEventListener`, `NotificationListener`, `ToastListener`, `GestureListener`
- **关键字段**：`broadcast` (BroadcastEmitter) — 广播发射器
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `emitter()` | 创建新的 EventEmitter |
| `observeKey()` | 开始监听按键事件 |
| `observeTouch()` | 开始监听触摸事件 |
| `observeNotification()` | 开始监听通知事件 |
| `observeToast()` | 开始监听 Toast 事件 |
| `observeGesture()` | 开始监听手势事件（API 26+） |
| `onKeyDown(String keyName, Object listener)` | 监听指定按键按下 |
| `onKeyUp(String keyName, Object listener)` | 监听指定按键抬起 |
| `onTouch(Object listener)` | 监听触摸事件 |
| `onNotification(Object listener)` | 监听通知事件 |
| `onToast(Object listener)` | 监听 Toast 事件 |
| `setKeyInterceptionEnabled(boolean)` | 拦截所有按键 |
| `setKeyInterceptionEnabled(String key, boolean)` | 拦截指定按键 |
| `setTouchEventTimeout(long)` | 设置触摸事件节流间隔（默认 10ms） |
| `recycle()` | 释放所有监听器 |

### Threads
- **职责**：多线程管理（线程创建、同步原语、生命周期控制）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `start(Runnable)` | 启动新线程（TimerThread），返回线程实例 |
| `currentThread()` | 获取当前线程（主线程返回 MainThreadProxy） |
| `getMainThread()` | 获取主线程引用 |
| `disposable()` | 创建 VolatileDispose 同步对象 |
| `atomic(long)` / `atomic()` | 创建 AtomicLong 原子变量 |
| `lock()` | 创建 ReentrantLock 锁 |
| `shutDownAll()` | 中断并清除所有子线程 |
| `exit()` | 关闭所有线程并标记退出 |
| `hasRunningThreads()` | 检查是否有运行中的子线程 |

### Files
- **职责**：文件系统操作（读写、复制、删除、路径处理）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `path(String relativePath)` | 相对路径转绝对路径（基于 cwd） |
| `cwd()` | 获取当前工作目录 |
| `open(String path, String mode, String encoding, int bufferSize)` | 打开文件 |
| `read(String path)` / `read(String path, String encoding)` | 读取文件内容 |
| `readBytes(String path)` | 读取字节数组 |
| `readAssets(String path)` | 读取 assets 文件 |
| `write(String path, String text)` | 写入文件 |
| `writeBytes(String path, byte[])` | 写入字节数组 |
| `append(String path, String text)` | 追加内容 |
| `copy(String from, String to)` | 复制文件 |
| `move(String path, String newPath)` | 移动文件 |
| `rename(String path, String newName)` | 重命名 |
| `remove(String path)` | 删除文件 |
| `removeDir(String path)` | 删除目录 |
| `listDir(String path)` / `listDir(String path, Func1 filter)` | 列出目录内容 |
| `exists(String)` / `isFile(String)` / `isDir(String)` / `isEmptyDir(String)` | 文件状态检查 |
| `create(String)` / `createIfNotExists(String)` / `createWithDirs(String)` | 创建文件/目录 |
| `ensureDir(String)` | 确保目录存在 |
| `join(String parent, String... child)` | 路径拼接 |
| `getExtension(String)` / `getName(String)` / `getNameWithoutExtension(String)` | 路径信息提取 |
| `getSdcardPath()` | 获取 SD 卡路径 |

### AppUtils
- **职责**：应用操作（启动、卸载、打开 URL、查看文件）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `launchPackage(String packageName)` | 通过包名启动应用 |
| `launchApp(String appName)` | 通过应用名启动应用 |
| `getPackageName(String appName)` | 根据应用名获取包名 |
| `getAppName(String packageName)` | 根据包名获取应用名 |
| `uninstall(String packageName)` | 卸载应用 |
| `openAppSetting(String packageName)` | 打开应用设置页 |
| `openUrl(String url)` | 在浏览器中打开 URL |
| `viewFile(String path)` | 查看文件 |
| `editFile(String path)` | 编辑文件 |
| `getCurrentActivity()` | 获取当前 Activity（弱引用） |
| `setCurrentActivity(Activity)` | 设置当前 Activity |

### Dialogs
- **职责**：对话框 API（alert、confirm、input、select、单选、多选）
- **继承**：无
- **关键字段**：`nonUiDialogs` (NonUiDialogs) — 非 UI 线程对话框
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `alert(String title, String content, Object callback)` | 弹出提示框 |
| `confirm(String title, String content, Object callback)` | 弹出确认框 |
| `rawInput(String title, String prefill, Object callback)` | 弹出输入框 |
| `select(String title, String[] items, Object callback)` | 弹出选择列表 |
| `singleChoice(String title, int selectedIndex, String[] items, Object callback)` | 单选对话框 |
| `multiChoice(String title, int[] indices, String[] items, Object callback)` | 多选对话框 |
| `selectFile(String title, String prefill, Object callback)` | 文件选择对话框 |
| `newBuilder()` | 创建自定义 MaterialDialog 构建器 |

### Floaty
- **职责**：悬浮窗管理（创建、定位、调整大小、关闭）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `window(View view)` | 创建可调整大小的悬浮窗 |
| `window(BaseResizableFloatyWindow.ViewSupplier)` | 通过 Supplier 创建悬浮窗 |
| `rawWindow(View view)` | 创建原始悬浮窗（自定义布局） |
| `rawWindow(RawWindow.RawFloaty)` | 通过 RawFloaty 创建悬浮窗 |
| `closeAll()` | 关闭所有悬浮窗 |
| `checkPermission()` | 检查悬浮窗权限 |
| `requestPermission()` | 请求悬浮窗权限 |

- **内部类 JsRawWindow**：

| 方法 | 说明 |
|------|------|
| `findView(String id)` | 根据 ID 查找视图 |
| `getX()` / `getY()` | 获取窗口位置 |
| `setPosition(int x, int y)` | 设置窗口位置 |
| `setSize(int w, int h)` | 设置窗口大小 |
| `setTouchable(boolean)` | 设置是否可触摸 |
| `requestFocus()` / `disableFocus()` | 焦点控制 |
| `exitOnClose()` | 关闭时退出脚本 |
| `close()` | 关闭窗口 |

### Sensors
- **职责**：传感器 API（加速度、陀螺仪、光线、距离等）
- **继承**：`EventEmitter` implements `Loopers.LooperQuitHandler`
- **支持的传感器**：ACCELEROMETER, MAGNETIC_FIELD, ORIENTATION, GYROSCOPE, LIGHT, TEMPERATURE, PRESSURE, PROXIMITY, GRAVITY, LINEAR_ACCELERATION, RELATIVE_HUMIDITY 等
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `register(String sensorName)` | 注册传感器监听（默认 normal 延迟） |
| `register(String sensorName, int delay)` | 注册传感器（指定延迟：normal/ui/game/fastest） |
| `unregister(SensorEventEmitter)` | 取消注册 |
| `unregisterAll()` | 取消所有注册 |
| `getSensor(String sensorName)` | 获取传感器实例 |

- **事件**：`change`（传感器数据变化）、`accuracy_change`（精度变化）、`unsupported_sensor`（不支持的传感器）

### Media
- **职责**：媒体操作（音乐播放、媒体扫描）
- **继承**：implements `MediaScannerConnection.MediaScannerConnectionClient`
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `scanFile(String path)` | 扫描文件并添加到媒体库 |
| `playMusic(String path)` / `playMusic(String, float volume)` / `playMusic(String, float, boolean looping)` | 播放音乐 |
| `pauseMusic()` / `resumeMusic()` / `stopMusic()` | 音乐控制 |
| `musicSeekTo(int millis)` | 跳转到指定位置 |
| `isMusicPlaying()` | 是否正在播放 |
| `getMusicDuration()` / `getMusicCurrentPosition()` | 获取时长/当前位置 |
| `recycle()` | 释放资源 |

### Engines
- **职责**：脚本引擎管理（执行脚本、停止引擎、获取引擎列表）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `execScript(String name, String script, ExecutionConfig)` | 执行脚本字符串 |
| `execScriptFile(String path, ExecutionConfig)` | 执行脚本文件 |
| `execAutoFile(String path, ExecutionConfig)` | 执行 .auto 文件 |
| `all()` | 获取所有运行中的引擎 |
| `stopAll()` | 停止所有引擎 |
| `myEngine()` | 获取当前引擎实例 |

### Timers
- **职责**：定时器管理（setTimeout、setInterval、setImmediate）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `setTimeout(Object callback, long delay, Object... args)` | 延时执行 |
| `clearTimeout(int id)` | 清除延时 |
| `setInterval(Object listener, long interval, Object... args)` | 循环执行 |
| `clearInterval(int id)` | 清除循环 |
| `setImmediate(Object listener, Object... args)` | 立即执行 |
| `clearImmediate(int id)` | 清除立即执行 |
| `hasPendingCallbacks()` | 检查是否有待执行回调 |
| `getMainTimer()` | 获取主线程定时器 |
| `getTimerForCurrentThread()` | 获取当前线程定时器 |

### UI
- **职责**：UI 布局系统（动态布局加载、数据绑定）
- **继承**：`ProxyObject`
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `getDynamicLayoutInflater()` | 获取动态布局加载器 |
| `getResourceParser()` | 获取资源解析器 |
| `getBindingContext()` / `setBindingContext(Object)` | 数据绑定上下文 |
| `getLayoutInflater()` | 获取布局加载器 |

### Console
- **职责**：控制台输出接口
- **类型**：interface
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `log(Object data, Object... options)` | DEBUG 级别日志 |
| `info(Object data, Object... options)` | INFO 级别日志 |
| `warn(Object data, Object... options)` | WARN 级别日志 |
| `error(Object data, Object... options)` | ERROR 级别日志 |
| `verbose(Object data, Object... options)` | VERBOSE 级别日志 |
| `assertTrue(boolean, Object, Object...)` | 断言 |
| `clear()` | 清空控制台 |
| `show()` / `show(boolean isAutoHide)` / `hide()` | 显示/隐藏控制台 |
| `setTitle(CharSequence, String color, int size)` | 设置标题 |
| `setBackground(String color)` | 设置背景色 |
| `setLogSize(int size)` | 设置字号 |
| `setMaxLines(int maxLines)` | 设置最大行数 |
| `setCanInput(boolean)` | 设置是否可输入 |

### AbstractConsole
- **职责**：Console 接口的基础实现
- **继承**：implements `Console`
- **核心方法**：格式化输出 `format(Object data, Object... options)`、分级打印 `printf(int level, ...)`

### Device
- **职责**：设备信息与硬件控制（屏幕、音量、电池、振动、WakeLock）
- **继承**：无
- **静态常量**：`width`, `height`, `model`, `brand`, `sdkInt`, `release`, `buildId`, `fingerprint`, `serial` 等
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `getIMEI()` / `getAndroidId()` / `getMacAddress()` | 设备标识 |
| `getBattery()` | 获取电量百分比 |
| `isCharging()` | 是否充电中 |
| `getBrightness()` / `setBrightness(int)` | 屏幕亮度 |
| `getMusicVolume()` / `setMusicVolume(int)` | 音乐音量 |
| `getAlarmVolume()` / `setAlarmVolume(int)` | 闹钟音量 |
| `getNotificationVolume()` / `setNotificationVolume(int)` | 通知音量 |
| `getTotalMem()` / `getAvailMem()` | 内存信息 |
| `isScreenOn()` / `wakeUp()` / `wakeUpIfNeeded()` | 屏幕状态 |
| `keepScreenOn()` / `keepScreenDim()` / `cancelKeepingAwake()` | WakeLock 控制 |
| `vibrate(long)` / `cancelVibration()` | 振动控制 |
| `checkDeviceHasNavigationBar()` | 检查是否有导航栏 |
| `getVirtualBarHeigh()` | 获取虚拟导航栏高度 |

### AbstractShell
- **职责**：Shell 命令执行基类（input、sendevent、screencap 等）
- **继承**：abstract
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `exec(String command)` | 执行 Shell 命令（抽象） |
| `Tap(int x, int y)` | 点击坐标 |
| `Swipe(int x1, int y1, int x2, int y2)` / `Swipe(..., int time)` | 滑动 |
| `KeyCode(int)` / `KeyCode(String)` | 发送按键 |
| `Home()` / `Back()` / `Power()` / `Menu()` | 系统按键 |
| `VolumeUp()` / `VolumeDown()` | 音量键 |
| `Input(String text)` / `Text(String text)` | 输入文本 |
| `Screencap(String path)` | 命令行截图 |
| `SendEvent(int device, int type, int code, int value)` | 发送 input event |
| `Touch(int x, int y)` | 触摸事件 |
| `SetScreenMetrics(int width, int height)` | 设置屏幕尺寸映射 |

### Plugins
- **职责**：插件系统（从其他 APK 加载插件脚本）
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `load(String packageName)` | 加载指定包名的插件 |
| `clear()` | 清除插件缓存 |

### SevenZip
- **职责**：7-Zip 压缩/解压操作
- **继承**：无
- **核心方法**：

| 方法签名 | 说明 |
|----------|------|
| `cmdExec(String cmdStr)` | 执行 7z 命令 |
| `A(String type, String dest, String src)` | 压缩文件/目录 |
| `A(String type, String dest, String src, String password)` | 带密码压缩 |
| `X(String filePath, String dirPath)` | 解压文件 |
| `X(String filePath, String dirPath, String password)` | 带密码解压 |

---

## JS 模块层详解 (assets/modules/)

### 核心 API 封装模块

#### `__images__.js`
- **职责**：图像处理 API 的 JS 层封装，扩展 OpenCV 功能
- **核心导出**：
  - `requestScreenCapture(landscape)` — 请求截屏权限（支持 Promise）
  - `captureScreen()` / `read(path)` / `copy(img)` / `load(url)` — 基础图像操作
  - `save(img, path, format, quality)` — 保存图片
  - `findImage(img, template, options)` — 模板匹配找图（options: threshold, region, level）
  - `matchTemplate(img, template, options)` — 多目标匹配，返回 MatchingResult
  - `findColor(img, color, options)` / `findColorInRegion()` / `findColorEquals()` — 找色
  - `findAllPointsForColor(img, color, options)` — 查找所有匹配颜色点
  - `findMultiColors(img, firstColor, paths, options)` — 多色查找
  - `detectsColor(img, color, x, y, threshold, algorithm)` — 颜色检测
  - `grayscale(img)` / `threshold()` / `adaptiveThreshold()` — 二值化
  - `blur()` / `medianBlur()` / `gaussianBlur()` — 模糊处理
  - `cvtColor(img, code)` — 颜色空间转换
  - `resize(img, size)` / `scale(img, fx, fy)` / `rotate(img, degree)` — 变换
  - `concat(img1, img2, direction)` — 图片拼接
  - `inRange(img, lower, upper)` / `interval(img, color, threshold)` — 颜色范围
  - `findCircles(grayImg, options)` — 霍夫圆检测
  - `fromBase64()` / `toBase64()` / `fromBytes()` / `toBytes()` / `readPixels()` — 编解码
  - **MatchingResult** 类：`first()`, `last()`, `best()`, `worst()`, `leftmost()`, `topmost()`, `sortBy(cmp)`
- **全局注入**：`requestScreenCapture`, `captureScreen`, `findImage`, `findColor` 等
- **颜色算法**：`diff`, `rgb`, `rgb+`, `equal`, `hs`

#### `__events__.js`
- **职责**：事件系统 JS 封装
- **核心导出**：`events` 对象（继承 runtime.events）
- **附加功能**：`events.__asEmitter__(obj, thread)` — 将对象转为事件发射器
- **全局常量**：`keys` — 按键映射（home, menu, back, volume_up, volume_down）

#### `__threads__.js`
- **职责**：多线程 JS 封装
- **核心导出**：`threads` 对象（继承 runtime.threads）
- **全局函数**：`sync(func, lock)` — 同步函数
- **Promise 扩展**：`Promise.prototype.wait()` — 阻塞等待 Promise 结果

#### `__files__.js`
- **职责**：文件操作 JS 封装
- **核心导出**：`files` 对象（继承 runtime.files）
- **全局函数**：`open(path, mode, encoding, bufferSize)` — 打开文件

#### `__http__.js`
- **职责**：HTTP 请求模块
- **核心导出**：`http` 对象
- **API 列表**：
  | 方法 | 说明 |
  |------|------|
  | `http.get(url, options, callback)` | GET 请求 |
  | `http.post(url, data, options, callback)` | POST 请求（form-urlencoded） |
  | `http.postJson(url, data, options, callback)` | POST JSON |
  | `http.postMultipart(url, files, options, callback)` | 上传文件 |
  | `http.request(url, options, callback)` | 通用请求 |
  | `http.client()` | 获取 OkHttpClient |
  | `http.buildRequest(url, options)` | 构建 Request 对象 |
- **响应对象**：`statusCode`, `statusMessage`, `headers`, `body.string()`, `body.json()`, `body.bytes()`

#### `__dialogs__.js`
- **职责**：对话框 JS 封装（支持 Promise 和回调两种模式）
- **核心导出**：`dialogs` 对象
- **API 列表**：
  | 方法 | 说明 |
  |------|------|
  | `dialogs.rawInput(title, prefill, callback)` | 输入框 |
  | `dialogs.input(title, prefill, callback)` | 输入框（eval 结果） |
  | `dialogs.alert(title, content, callback)` | 提示框 |
  | `dialogs.confirm(title, content, callback)` | 确认框 |
  | `dialogs.select(title, items, callback)` | 选择列表 |
  | `dialogs.singleChoice(title, items, index, callback)` | 单选 |
  | `dialogs.multiChoice(title, items, index, callback)` | 多选 |
  | `dialogs.build(properties)` | 自定义对话框构建器 |
- **build 属性**：title, content, items, positive/negative/neutral, progress, customView, inputHint, itemsSelectMode
- **全局注入**：`rawInput`, `alert`, `confirm`, `prompt`

#### `__ui__.js`
- **职责**：UI 布局与数据绑定系统
- **核心导出**：`ui` 对象
- **API 列表**：
  | 方法 | 说明 |
  |------|------|
  | `ui.layout(xml)` | 设置 Activity 布局 |
  | `ui.layoutFile(file)` | 从文件加载布局 |
  | `ui.inflate(xml, parent, attachToParent)` | 动态加载视图 |
  | `ui.setContentView(view)` | 设置内容视图 |
  | `ui.findById(id)` / `ui.findView(id)` | 查找视图 |
  | `ui.run(action)` | 在 UI 线程执行 |
  | `ui.post(action, delay)` | 延迟在 UI 线程执行 |
  | `ui.statusBarColor(color)` | 设置状态栏颜色 |
  | `ui.registerWidget(name, widget)` | 注册自定义控件 |
  | `ui.emitter` | Activity 事件发射器 |
- **Widget 系统**：`ui.Widget` 基类，支持 `render()`, `defineAttr()`, `onViewCreated()`, `onFinishInflation()`
- **数据绑定**：`{{expression}}` 模板语法，绑定到全局作用域

#### `__automator__.js`
- **职责**：UI 自动化操作 JS 封装
- **核心导出**：`automator` 对象 + `auto` 全局对象
- **自动化方法**（全局注入）：
  | 方法 | 说明 |
  |------|------|
  | `click(x, y)` / `click(text, index)` / `click(rect)` | 点击 |
  | `longClick(...)` | 长按 |
  | `press(x, y, duration)` | 按压 |
  | `swipe(x1, y1, x2, y2, duration)` | 滑动 |
  | `gesture(duration, [x1,y1], [x2,y2], ...)` | 手势 |
  | `gestures([delay, duration, [points]...])` | 多指手势 |
  | `scrollDown()` / `scrollUp()` | 滚动 |
  | `setText(index, text)` | 设置文本 |
  | `input(index, text)` | 追加文本 |
- **auto 对象**：
  | 方法/属性 | 说明 |
  |-----------|------|
  | `auto(mode)` | 确保无障碍服务开启 |
  | `auto.waitFor()` | 等待无障碍服务开启 |
  | `auto.setMode("normal"/"fast")` | 设置模式 |
  | `auto.setFlags(flags)` | 设置标志 |
  | `auto.service` | 获取无障碍服务实例 |
  | `auto.root` / `auto.rootInActiveWindow` | 获取窗口根节点 |
  | `auto.windows` / `auto.windowRoots` | 获取窗口列表 |
- **全局操作**：`back`, `home`, `powerDialog`, `notifications`, `recents`, `takeScreenshot`, `lockScreen` 等

#### `__selector__.js`
- **职责**：UI 控件选择器封装
- **核心导出**：`selector()` 函数
- **全局注入**：选择器的所有方法（如 `id()`, `text()`, `desc()`, `className()` 等）

#### `__console__.js`
- **职责**：控制台 API 封装
- **核心导出**：`console` 对象
- **API**：`log`, `info`, `warn`, `error`, `verbose`, `assert`, `time`, `timeEnd`, `trace`, `show`, `hide`, `clear`, `setGlobalLogConfig`, `setPosition`, `setTitle`, `setBackground`, `setLogSize`, `setMaxLines`, `setCanInput`
- **全局注入**：`print`, `log`, `err`, `openConsole`, `clearConsole`

#### `__engines__.js`
- **职责**：脚本引擎管理
- **核心导出**：`engines` 对象
- **API**：`execScript(name, script, config)`, `execScriptFile(path, config)`, `execAutoFile(path, config)`, `myEngine()`, `all()`, `stopAll()`
- **config 参数**：`path`, `delay`, `interval`, `loopTimes`, `arguments`

#### `__floaty__.js`
- **职责**：悬浮窗 JS 封装
- **核心导出**：`floaty` 对象
- **API**：`floaty.window(xml)`, `floaty.rawWindow(xml)`, `floaty.closeAll()`, `floaty.checkPermission()`, `floaty.requestPermission()`

#### `__sensors__.js`
- **职责**：传感器（直接继承 runtime.sensors）
#### `__media__.js`
- **职责**：媒体操作（直接继承 runtime.media）
#### `__storages__.js`
- **职责**：本地存储（JSON 序列化）
- **API**：`storages.create(name)` → LocalStorage（`put`, `get`, `remove`, `contains`, `clear`）
#### `__paddle__.js`
- **职责**：PaddleOCR 封装
- **API**：`paddle.ocr(image)` → OCR 结果数组, `paddle.ocrText(image)` → 纯文本数组
#### `__web__.js`
- **职责**：WebView 和 WebSocket
- **API**：`newInjectableWebClient()`, `newInjectableWebView(activity)`, `newWebSocket(url, options)`

### 全局函数模块

#### `__globals__.js`
- **职责**：全局函数注入
- **注入的全局函数**：
  | 函数 | 说明 |
  |------|------|
  | `toast(text)` | 显示 Toast |
  | `toastLog(text)` | 显示 Toast 并记录日志 |
  | `sleep(ms)` | 暂停（不可在 UI 线程使用） |
  | `exit()` / `stop()` | 退出脚本 |
  | `isStopped()` / `isRunning()` | 脚本状态检查 |
  | `setClip(text)` / `getClip()` | 剪贴板操作 |
  | `currentPackage()` / `currentActivity()` | 当前前台信息 |
  | `waitForActivity(name, period)` | 等待指定 Activity |
  | `waitForPackage(name, period)` | 等待指定包名 |
  | `random(min, max)` | 随机数 |
  | `setScreenMetrics(w, h)` | 设置屏幕尺寸映射 |
  | `requiresApi(level)` / `requiresAutojsVersion(v)` | 版本要求检查 |
- **全局对象**：`zips`（压缩）, `gmlkit`（Google ML Kit OCR）

### 工具模块

| 模块文件 | 功能 |
|----------|------|
| `__util__.js` | 工具函数（format, java 等） |
| `__io__.js` | IO 操作封装 |
| `__java_util__.js` | Java 工具类映射 |
| `__json2__.js` | JSON polyfill |
| `__shell__.js` | Shell 命令封装 |
| `__timers__.js` | 定时器封装 |
| `__plugins__.js` | 插件系统封装 |
| `__bridges__.js` | Java/JS 桥接层 |
| `__continuation__.js` | 协程/continuation 支持 |
| `__RootAutomator__.js` | Root 自动化 |
| `__unit_test__.js` | 单元测试框架 |
| `__$base64__.js` | Base64 编解码 |
| `__$crypto__.js` | 加密解密 |
| `__$zip__.js` | 压缩解压 |

### 第三方库

| 模块文件 | 功能 |
|----------|------|
| `lodash.js` | Lodash 工具库 |
| `promise.js` | Promise polyfill |
| `jvm-npm.js` | CommonJS 模块加载器 |
| `result_adapter.js` | Java 异步结果适配器 |
| `vconsole.min.js` | 移动端调试控制台 |
| `rescale.js` | 图像缩放工具 |
| `array-observe.min.js` | Array.observe polyfill |
| `object-observe-lite.min.js` | Object.observe polyfill |
