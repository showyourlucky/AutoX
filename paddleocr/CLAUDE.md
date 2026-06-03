[根目录](../CLAUDE.md) > **paddleocr**

# paddleocr 模块

## 变更记录 (Changelog)

| 日期 | 变更内容 |
|------|---------|
| 2026-06-03 | 初始生成 |

---

## 模块职责

PaddleOCR 文字识别封装模块，基于百度 Paddle Lite 推理引擎，提供中文 OCR 能力。支持自定义模型加载、slim 精简模型、多线程推理。

---

## 入口与启动

| 文件 | 作用 |
|------|------|
| `src/main/java/com/baidu/paddle/lite/demo/ocr/Predictor.kt` | OCR 预测器主类 |
| `src/main/java/com/baidu/paddle/lite/demo/ocr/OCRPredictorNative.kt` | Native JNI 接口 |
| `src/main/java/com/baidu/paddle/lite/demo/ocr/Utils.kt` | 工具函数 |

---

## 对外接口

### Predictor 类

| 方法 | 功能 |
|------|------|
| `initOcr(context, cpuThreadNum, useSlim)` | 初始化 OCR（内置模型） |
| `initOcr(context, cpuThreadNum, myModelPath)` | 初始化 OCR（自定义模型） |
| `runOcr(bitmap, cpuThreadNum)` | 执行 OCR 识别 |
| `ocr(context, bitmap, cpuThreadNum, useSlim)` | 初始化 + 识别一步完成 |
| `ocrText(context, bitmap, cpuThreadNum, useSlim)` | 返回纯文本数组 |
| `releaseOcr()` | 释放模型资源 |
| `checkInitSuccess()` | 校验模型是否正确加载 |

### 数据模型

| 类 | 功能 |
|----|------|
| `OcrResult.kt` | OCR 结果（text、confidence、bounds、preprocessTime、inferenceTime） |
| `OcrResultModel.kt` | 内部结果模型（wordIndex、points、label） |

### 模型文件

- 内置 slim 模型：`assets/models/ocr_v2_for_cpu(slim)/`
- 内置完整模型：`assets/models/ocr_v2_for_cpu/`
- 标签文件：`assets/labels/ppocr_keys_v1.txt`
- 模型格式：Paddle Lite `.nb` 格式
- 模型组件：det（检测）、rec（识别）、cls（分类）

---

## 关键依赖与配置

- Paddle Lite（通过 JNI 调用 Native 库）
- Native SO 文件：`libpaddle_light_api_shared.so`

---

## 测试与质量

- `src/test/java/org/autojs/autoxjs/paddleocr/ExampleUnitTest.kt`（占位）

---

## 常见问题 (FAQ)

**Q: useSlim=true 和 false 的区别？**
A: slim 是精简模型，体积更小、速度更快，但精度略低。默认使用 slim 模型。

**Q: 如何使用自定义模型？**
A: 调用 `predictor.init(context, myModelPath)` 指定自定义模型路径，模型目录需包含 det/rec/cls 三个 `.nb` 文件。

---

## 相关文件清单

- `paddleocr/build.gradle.kts` -- 构建配置
- `paddleocr/src/main/java/com/baidu/paddle/lite/demo/ocr/` -- 源码
- `paddleocr/src/main/assets/models/` -- OCR 模型文件
- `paddleocr/src/main/assets/labels/` -- 标签文件
