# 网易云音乐翻译模块

LSPosed/Xposed 模块，将网易云音乐界面实时翻译为越南语。

## 特性

- ✅ **运行时翻译**：不修改 APK，完全绕过签名验证
- ✅ **版本兼容**：应用更新后自动生效，无需重新注入
- ✅ **高性能**：直接 Hook Resources 方法，性能损耗极小
- ✅ **可扩展**：支持外部词典文件，可随时更新翻译
- ✅ **内置词典**：包含 50+ 常用界面词汇

## 系统要求

- Android 8.0+ (API 26+)
- Root 权限
- LSPosed 或 Xposed 框架已安装

## 安装步骤

### 1. 安装 LSPosed 框架

如果你还没有安装 LSPosed：

```bash
# 需要先安装 Magisk
# 然后在 Magisk 中安装 LSPosed 模块
# 下载地址: https://github.com/LSPosed/LSPosed/releases
```

### 2. 安装翻译模块

1. 下载 `NeteaseMusicTranslator.apk`
2. 安装到设备上
3. 打开 LSPosed Manager
4. 进入"模块"页面，激活"网易云音乐翻译"
5. 点击模块名称，勾选作用域：`com.netease.cloudmusic`
6. 重启网易云音乐应用

### 3. 验证安装

1. 打开网易云音乐
2. 在 LSPosed 日志中查看是否有 `NeteaseMusicTranslator: 注入完成` 消息
3. 界面中的中文应该已经被翻译为越南语

## 使用外部词典（可选）

模块支持从外部 JSON 文件加载扩展翻译词典：

### 1. 创建词典文件

在设备存储根目录创建 `netease_translations.json`：

```bash
adb push netease_translations.json /sdcard/
```

### 2. 词典格式

```json
{
  "下载": "Tải xuống",
  "播放": "Phát",
  "我的": "Của tôi",
  "发现": "Khám phá"
}
```

### 3. 重新加载词典

重启网易云音乐应用即可自动加载新词典。

## 开发和构建

### 环境要求

- JDK 11+
- Android SDK (API 34)
- Gradle 8.0+

### 构建 APK

```bash
# 克隆项目
git clone <repository-url>
cd NeteaseMusicTranslator

# 构建 Debug 版本
gradlew assembleDebug

# APK 输出路径
# app/build/outputs/apk/debug/app-debug.apk
```

### 项目结构

```
NeteaseMusicTranslator/
├── app/
│   ├── src/main/
│   │   ├── java/com/netease/musictranslator/
│   │   │   ├── XposedModule.java           # Xposed 核心 Hook 逻辑
│   │   │   ├── TranslationDictionary.java  # 词典管理器
│   │   │   └── MainActivity.java           # 配置界面
│   │   ├── res/
│   │   │   └── values/strings.xml          # 字符串资源
│   │   ├── assets/
│   │   │   └── xposed_init                 # Xposed 模块声明
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 工作原理

模块通过 LSPosed/Xposed 框架在运行时 Hook Android 的 `Resources` 类：

```java
// Hook Resources.getString(int id)
Resources.getString(int id) {
    String original = super.getString(id);  // 获取原始中文
    String translated = dictionary.get(original);
    return translated != null ? translated : original;
}
```

当网易云音乐调用 `getString()` 获取界面文本时，模块拦截调用并返回越南语翻译。

## 扩展翻译词典

内置词典包含常用词汇，你可以通过以下方式扩展：

### 方法 1: 外部 JSON 文件

在 `/sdcard/netease_translations.json` 中添加新的翻译。

### 方法 2: 修改源代码

编辑 `TranslationDictionary.java` 的 `loadBuiltInTranslations()` 方法：

```java
translations.put("新词汇", "Từ mới");
```

重新构建并安装模块。

## 故障排查

### 模块未生效

1. 确认 LSPosed 已正确安装并激活
2. 确认模块已在 LSPosed 中启用
3. 确认已勾选作用域 `com.netease.cloudmusic`
4. 尝试重启设备

### 查看日志

```bash
# 查看 LSPosed 日志
adb logcat | grep NeteaseMusicTranslator

# 或在 LSPosed Manager 中查看模块日志
```

### 部分文本未翻译

可能原因：
1. 该文本不在词典中
2. 文本是动态生成的（如服务器返回）
3. 文本使用了特殊的加载方式

解决方法：
1. 添加该文本到外部词典
2. 检查 LSPosed 日志，查看原始文本
3. 提交 Issue 或 Pull Request

## 技术细节

### Hook 的方法

- `Resources.getString(int id)`
- `Resources.getText(int id)`
- `Resources.getString(int id, Object... formatArgs)`

### 性能优化

- 使用 HashMap 存储翻译，O(1) 查找时间
- 只在文本存在翻译时才替换，减少不必要的操作
- 词典一次加载，重复使用

### 兼容性

- 支持 Android 8.0+ (API 26+)
- 兼容 LSPosed 和传统 Xposed
- 网易云音乐所有版本通用

## 贡献

欢迎提交 Pull Request 或 Issue！

### 贡献翻译词汇

如果你发现未翻译的词汇，可以：

1. 提交包含新词汇的 JSON 文件
2. 或直接修改 `TranslationDictionary.java`

### 代码规范

- 遵循 Java 代码规范
- 添加适当的注释
- 测试后再提交

## 许可证

MIT License

## 致谢

- [LSPosed](https://github.com/LSPosed/LSPosed) - 现代化的 Xposed 框架
- [Xposed Framework](https://github.com/rovo89/Xposed) - Android Hook 框架
- 网易云音乐 - 目标应用

## 免责声明

本项目仅用于学习和研究目的。请在合法范围内使用。
