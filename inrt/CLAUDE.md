[根目录](../CLAUDE.md) > **inrt**

# inrt 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

打包后的脚本运行时（InRT = Instant Runtime）。当用户通过 app 模块将 JS 脚本打包成独立 APK 时，inrt 模块提供运行时模板（template APK），使打包后的应用能独立运行用户脚本，无需安装 AutoX.js 主程序。

---

## 入口与启动

| 文件 | 作用 |
|------|------|
| `src/main/java/com/stardust/auojs/inrt/SplashActivity.kt` | 启动页，权限检查后启动脚本 |
| `src/main/java/com/stardust/auojs/inrt/App.kt` | Application 类 |
| `src/main/java/com/stardust/auojs/inrt/launch/GlobalProjectLauncher.kt` | 全局项目启动器 |
| `src/main/java/com/stardust/auojs/inrt/launch/AssetsProjectLauncher.kt` | 从 assets 加载项目 |

### 启动流程

1. `SplashActivity.onCreate()` 读取 `assets/project/` 下的 `project.json` 配置
2. 检查权限（无障碍服务、悬浮窗、后台启动、存储）
3. 全部权限就绪后调用 `GlobalProjectLauncher.launch()` 执行脚本

---

## 对外接口

### 构建任务

| Gradle Task | 功能 |
|-------------|------|
| `inrt:assembleTemplateDebug` | 编译 template 调试版 |
| `inrt:assembleTemplate` | 编译 template 发布版 |
| `inrt:cp2APPDebug` | 复制到 app/assets（调试） |
| `inrt:cp2APP` | 复制到 app/assets（发布） |

### 远程连接

| 类 | 功能 |
|----|------|
| `pluginclient/DevPluginService.java` | 开发插件服务 |
| `pluginclient/JsonWebSocket.java` | WebSocket 通信 |
| `pluginclient/Router.java` | 消息路由 |
| `pluginclient/AutoXKeepLiveService.java` | 保活服务 |

---

## 关键依赖与配置

- applicationId: `org.autojs.autoxjs.inrt`
- 依赖：autojs、automator、common
- 产品变体：`common`（通用版）、`template`（模板版，无 JNI 库）

---

## 测试与质量

- `src/test/java/com/stardust/auojs/inrt/ExampleUnitTest.java`（占位）

---

## 常见问题 (FAQ)

**Q: template 和 common 变体的区别？**
A: template 变体会在 mergeAssets 后删除 models、mlkit-google-ocr-models、project 目录，且不包含 JNI 库，用于嵌入到 app 的 assets 中作为打包模板。common 变体是完整可独立运行的版本。

---

## 相关文件清单

- `inrt/build.gradle.kts` -- 构建配置（含 cp2APP 任务）
- `inrt/src/main/java/com/stardust/auojs/inrt/` -- 源码
- `inrt/src/main/res-i18n/` -- 国际化资源
