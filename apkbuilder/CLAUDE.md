[根目录](../CLAUDE.md) > **apkbuilder**

# apkbuilder 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

APK 构建工具库，提供从模板 APK 修改资源、编辑 Manifest、替换文件、重新打包等能力。是"将 JS 脚本打包为独立 APK"功能的底层实现。

---

## 入口与启动

| 文件 | 作用 |
|------|------|
| `src/main/java/com/stardust/autojs/apkbuilder/ApkBuilder.java` | APK 构建器主类 |
| `src/main/java/com/stardust/autojs/apkbuilder/ManifestEditor.java` | AndroidManifest.xml 编辑器 |
| `src/main/java/com/stardust/autojs/apkbuilder/ApkPackager.java` | APK 解压/打包 |

### 构建流程

1. `ApkBuilder(inputStream, outFile, workspace)` -- 从模板 APK 创建构建器
2. `prepare()` -- 解压模板 APK 到工作目录
3. `editManifest()` -- 修改 Manifest（包名、版本、应用名等）
4. 修改资源文件、替换 assets
5. `build()` -- 重新打包为 APK

---

## 对外接口

### 核心类

| 类 | 功能 |
|----|------|
| `ApkBuilder.java` | APK 构建主类 |
| `ManifestEditor.java` | Manifest XML 编辑 |
| `ApkPackager.java` | APK 解压打包 |
| `ArscUtil.kt` | ARSC 资源修改工具 |

### 资源解码器（zhao.arsceditor.*）

| 类 | 功能 |
|----|------|
| `ARSCDecoder.java` | ARSC 二进制资源解析 |
| `AXMLDecoder.java` | AXML 二进制 XML 解析 |
| `AndrolibResources.java` | 资源操作封装 |

### AXML 处理（pxb.android.axml.*）

| 类 | 功能 |
|----|------|
| `AxmlParser.java` | AXML 解析器 |
| `AxmlWriter.java` | AXML 写入器 |
| `DumpEditor.java` | XML 转储编辑器 |

---

## 关键依赖与配置

- Apache Commons IO
- 纯 Java 实现，无 Android 特定依赖

---

## 测试与质量

无测试文件。

---

## 相关文件清单

- `apkbuilder/build.gradle.kts` -- 构建配置
- `apkbuilder/src/main/java/com/stardust/autojs/apkbuilder/` -- 核心构建逻辑
- `apkbuilder/src/main/java/pxb/android/` -- AXML/ARSC 底层处理
- `apkbuilder/src/main/java/zhao/arsceditor/` -- 资源编辑器
