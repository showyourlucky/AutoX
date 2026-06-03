[根目录](../CLAUDE.md) > **common**

# common 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

通用基础工具库，提供文件 IO、并发工具、权限管理、加解密、网络工具、UI 工具等底层能力。被 autojs、automator、app、inrt 等多个模块依赖。

---

## 入口与启动

无独立入口，作为工具库被其他模块引用。

---

## 对外接口

### 文件 IO（pio/）

| 类 | 功能 |
|----|------|
| `PFile.java` / `PFiles.kt` | 文件操作工具（读写、复制、删除） |
| `PReadableTextFile.java` | 文本文件读取 |
| `PWritableTextFile.java` | 文本文件写入 |
| `PReadableBinaryFile.java` | 二进制文件读取 |
| `PRandomAccessBinaryFile.java` | 随机访问文件 |
| `StreamUtils.kt` | 流工具 |

### 应用工具（app/）

| 类 | 功能 |
|----|------|
| `GlobalAppContext.kt` | 全局 Application 上下文 |
| `DialogUtils.java` | 对话框工具 |
| `permission/Permissions.kt` | 权限常量定义 |
| `permission/DrawOverlaysPermission.kt` | 悬浮窗权限 |
| `permission/BackgroundStartPermission.kt` | 后台启动权限 |

### 并发工具（concurrent/）

| 类 | 功能 |
|----|------|
| `VolatileDispose.java` | 阻塞等待/通知 |
| `VolatileBox.java` | 线程安全值容器 |
| `ConcurrentArrayList.java` | 线程安全列表 |

### 工具类（util/）

| 类 | 功能 |
|----|------|
| `ScreenMetrics.java` | 屏幕尺寸 |
| `MD5.java` | MD5 哈希 |
| `NetworkUtils.java` | 网络工具 |
| `ClipboardUtil.java` | 剪贴板 |
| `IntentUtil.java` | Intent 工具 |
| `AdvancedEncryptionStandard.kt` | AES 加解密 |
| `UiHandler.java` | UI 线程 Handler |

### IO 扩展（io/）

| 类 | 功能 |
|----|------|
| `Zip.kt` | ZIP 压缩/解压 |
| `ByteBufferBackedInputStream.kt` | ByteBuffer 输入流 |
| `ConcatReader.java` | 多 Reader 合并 |

---

## 关键依赖与配置

- 无第三方库依赖（纯 Android SDK）

---

## 测试与质量

- `src/test/java/com/stardust/ExampleUnitTest.java`（占位）

---

## 相关文件清单

- `common/build.gradle.kts` -- 构建配置
- `common/src/main/java/com/stardust/` -- 源码目录
