# 网易云音乐翻译模块 - 使用指南

## 快速开始

### 第一步：准备环境

确保你的 Android 设备满足以下条件：

- ✅ 已 Root（通过 Magisk）
- ✅ 已安装 LSPosed 框架
- ✅ Android 8.0 或更高版本

### 第二步：安装模块

1. **构建 APK**（开发者）或下载已构建的 APK

   ```bash
   cd D:\cd\wyyyy\software-development\NeteaseMusicTranslator
   gradlew assembleDebug
   ```

2. **安装到设备**

   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **在 LSPosed 中激活**
   
   - 打开 LSPosed Manager
   - 点击"模块"标签
   - 找到"网易云音乐翻译"并启用
   - 点击模块名称进入详情
   - 勾选"com.netease.cloudmusic"作为作用域
   - 保存设置

### 第三步：重启应用

强制停止并重新打开网易云音乐：

```bash
adb shell am force-stop com.netease.cloudmusic
adb shell am start -n com.netease.cloudmusic/.activity.MainActivity
```

### 第四步：验证效果

打开网易云音乐，检查界面文本：

- "下载" 应显示为 "Tải xuống"
- "播放" 应显示为 "Phát"
- "我的" 应显示为 "Của tôi"

## 使用外部词典

### 推送词典文件到设备

```bash
# 将示例词典推送到设备
adb push netease_translations.json /sdcard/

# 验证文件是否存在
adb shell ls -l /sdcard/netease_translations.json
```

### 添加自定义翻译

编辑 `netease_translations.json`，添加你需要的翻译：

```json
{
  "你的中文词汇": "Bản dịch tiếng Việt",
  "另一个词汇": "Bản dịch khác"
}
```

推送更新后的文件：

```bash
adb push netease_translations.json /sdcard/
```

重启网易云音乐以应用新的翻译。

## 查看日志

### 使用 adb logcat

实时查看模块日志：

```bash
adb logcat | grep NeteaseMusicTranslator
```

你应该看到类似的输出：

```
NeteaseMusicTranslator: 开始注入网易云音乐
NeteaseMusicTranslator: 已加载 50 条内置翻译
NeteaseMusicTranslator: 外部词典加载成功，新增/覆盖 10 条翻译
NeteaseMusicTranslator: 注入完成，已加载 60 条翻译
NeteaseMusicTranslator: [翻译] 下载 → Tải xuống
NeteaseMusicTranslator: [翻译] 播放 → Phát
```

### 使用 LSPosed Manager

1. 打开 LSPosed Manager
2. 点击"日志"标签
3. 筛选"网易云音乐翻译"模块

## 故障排查

### 问题 1: 模块未生效

**症状**：安装模块后，网易云音乐界面仍显示中文

**解决方法**：

1. 确认 LSPosed 框架已正确安装：
   ```bash
   adb shell su -c "ls /data/adb/lspd"
   ```

2. 确认模块已启用：
   - 打开 LSPosed Manager → 模块
   - 检查"网易云音乐翻译"是否有绿色勾选

3. 确认作用域已设置：
   - 点击模块 → 应用作用域
   - 确认 "com.netease.cloudmusic" 已勾选

4. 重启设备：
   ```bash
   adb reboot
   ```

### 问题 2: 外部词典未加载

**症状**：外部 JSON 文件中的翻译未生效

**解决方法**：

1. 确认文件位置正确：
   ```bash
   adb shell ls -l /sdcard/netease_translations.json
   ```

2. 确认 JSON 格式正确：
   ```bash
   # 推送文件后验证
   adb shell cat /sdcard/netease_translations.json
   ```

3. 检查权限：
   ```bash
   adb shell chmod 644 /sdcard/netease_translations.json
   ```

4. 查看日志确认加载状态：
   ```bash
   adb logcat | grep "外部词典"
   ```

### 问题 3: 部分文本未翻译

**原因**：

- 该文本不在词典中
- 文本是动态生成的（如用户名、歌曲名）
- 文本来自服务器

**解决方法**：

1. 使用 logcat 查看未翻译的原始文本：
   ```bash
   adb logcat | grep getString
   ```

2. 将新词汇添加到 `netease_translations.json`

3. 推送并重启应用

## 高级技巧

### 技巧 1: 批量收集未翻译词汇

```bash
# 运行网易云音乐并保存日志
adb logcat -c  # 清空日志
adb logcat | grep "getString" > untranslated.log

# 操作应用一段时间后，分析日志文件
# 提取所有 getString 调用的文本
```

### 技巧 2: 动态更新词典

不需要重启应用即可更新词典：

1. 修改本地 `netease_translations.json`
2. 推送到设备：
   ```bash
   adb push netease_translations.json /sdcard/
   ```
3. 强制停止应用：
   ```bash
   adb shell am force-stop com.netease.cloudmusic
   ```
4. 立即重新启动：
   ```bash
   adb shell am start -n com.netease.cloudmusic/.activity.MainActivity
   ```

### 技巧 3: 导出当前词典

从设备导出当前使用的词典：

```bash
adb pull /sdcard/netease_translations.json ./exported_dict.json
```

## 开发调试

### 修改代码后快速测试

```bash
# 1. 修改代码
# 2. 构建 APK
gradlew assembleDebug

# 3. 安装到设备（覆盖安装）
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 4. 重启网易云音乐
adb shell am force-stop com.netease.cloudmusic
adb shell am start -n com.netease.cloudmusic/.activity.MainActivity

# 5. 查看日志
adb logcat -c
adb logcat | grep NeteaseMusicTranslator
```

### 添加新的 Hook 点

如果需要 Hook 更多方法（如 TextView.setText），编辑 `XposedModule.java`：

```java
private void hookTextViewSetText(XC_LoadPackage.LoadPackageParam lpparam) {
    XposedHelpers.findAndHookMethod(
        "android.widget.TextView",
        lpparam.classLoader,
        "setText",
        CharSequence.class,
        new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                CharSequence original = (CharSequence) param.args[0];
                if (original != null) {
                    String translated = dictionary.translate(original.toString());
                    if (translated != null) {
                        param.args[0] = translated;
                    }
                }
            }
        }
    );
}
```

然后在 `handleLoadPackage()` 中调用：

```java
hookTextViewSetText(lpparam);
```

## 性能优化建议

1. **精简词典**：只保留实际使用的翻译，删除未出现的词汇
2. **使用内置词典**：常用词汇应编译到模块中，减少文件 I/O
3. **监控日志大小**：如果启用了详细日志，定期清理日志文件

## 常见问题 FAQ

**Q: 模块是否会影响应用性能？**

A: 影响极小。HashMap 查找是 O(1) 操作，只在文本有翻译时才替换。

**Q: 应用更新后需要重新安装模块吗？**

A: 不需要。模块在运行时注入，应用更新后自动生效。

**Q: 可以用于其他应用吗？**

A: 可以。修改 `TARGET_PACKAGE` 常量为目标应用包名即可。

**Q: 支持其他语言吗？**

A: 支持。修改词典中的翻译为任意语言即可。

**Q: 是否会被网易云音乐检测？**

A: LSPosed 在系统层运行，应用难以检测。但不保证未来版本不会加入检测机制。

## 获取帮助

如有问题，请：

1. 查看日志输出
2. 检查本文档的故障排查部分
3. 提交 Issue 并附上日志

---

祝使用愉快！🎵
