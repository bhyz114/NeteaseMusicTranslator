# 网易云音乐翻译模块 - 测试指南

## 📋 测试前准备

### 必需环境
- ✅ Android 8.0+ 设备（推荐 Android 10+）
- ✅ Root 权限（Magisk）
- ✅ LSPosed 框架已安装并激活
- ✅ 网易云音乐应用（任意版本）

### 下载 APK
1. 访问 GitHub Actions: https://github.com/bhyz114/NeteaseMusicTranslator/actions
2. 点击最新的成功构建（绿色勾选）
3. 向下滚动到 "Artifacts" 部分
4. 下载 `NeteaseMusicTranslator-debug.zip`
5. 解压得到 `app-debug.apk`

---

## 🔧 安装步骤

### 1. 安装 APK
```bash
# 通过 ADB 安装
adb install app-debug.apk

# 或直接在设备上点击安装
```

### 2. 在 LSPosed Manager 中激活模块
1. 打开 LSPosed Manager
2. 进入 "模块" 标签页
3. 找到 "网易云音乐翻译" 并勾选激活
4. 点击模块名称进入配置

### 3. 设置作用域
在模块配置页面：
1. 点击 "应用范围"
2. 搜索并勾选 `com.netease.cloudmusic`
3. 确认保存

### 4. 重启网易云音乐
```bash
# 强制停止并重启
adb shell am force-stop com.netease.cloudmusic
adb shell am start -n com.netease.cloudmusic/.activity.MainActivity
```

---

## ✅ 测试用例

### 测试 1: 模块注入验证
**目标**: 确认模块已成功注入网易云音乐

**步骤**:
1. 打开 LSPosed Manager
2. 进入 "日志" 页面
3. 筛选应用: `com.netease.cloudmusic`
4. 重启网易云音乐

**预期结果**:
```
NeteaseMusicTranslator: 模块已注入网易云音乐
NeteaseMusicTranslator: Hook Resources.getString(int)
NeteaseMusicTranslator: Hook Resources.getText(int)
NeteaseMusicTranslator: Hook Resources.getString(int, Object...)
NeteaseMusicTranslator: 加载内置词典: 50+ 条翻译
```

**判定**: 如果看到以上日志，说明注入成功 ✅

---

### 测试 2: 内置翻译验证
**目标**: 验证内置的 50+ 越南语翻译是否生效

**步骤**:
1. 打开网易云音乐主界面
2. 查看以下常见元素的文本

**预期翻译对照表**:

| 原中文 | 预期越南语 | 位置 |
|--------|-----------|------|
| 发现 | Khám phá | 底部导航栏 |
| 播客 | Podcast | 底部导航栏 |
| 我的 | Của tôi | 底部导航栏 |
| 云村 | Làng mây | 底部导航栏 |
| 账号 | Tài khoản | 底部导航栏 |
| 推荐 | Đề xuất | 首页顶部 |
| 排行榜 | Bảng xếp hạng | 首页 |
| 歌单 | Danh sách phát | 首页 |
| 电台 | Đài | 首页 |
| 歌手 | Ca sĩ | 首页 |
| 最新 | Mới nhất | 分类标签 |
| 播放 | Phát | 播放器 |
| 暂停 | Tạm dừng | 播放器 |
| 下载 | Tải xuống | 播放器 |
| 收藏 | Yêu thích | 播放器 |
| 分享 | Chia sẻ | 播放器 |
| 评论 | Bình luận | 播放器 |
| 搜索 | Tìm kiếm | 顶部搜索框 |
| 设置 | Cài đặt | 设置页面 |
| 登录 | Đăng nhập | 登录页面 |
| 注册 | Đăng ký | 登录页面 |

**判定**: 如果以上至少 80% 的文本显示为越南语，说明翻译生效 ✅

---

### 测试 3: 未翻译词汇回退
**目标**: 验证未翻译的词汇仍显示原中文

**步骤**:
1. 浏览网易云音乐各个页面
2. 观察是否有未翻译的中文文本

**预期结果**:
- 内置词典中的词汇显示越南语
- 未收录的词汇显示原中文
- 不应出现空白或乱码

**判定**: 如果未翻译的文本正常显示中文（不是空白），说明回退机制正常 ✅

---

### 测试 4: 外部词典加载（可选）
**目标**: 验证外部 JSON 词典文件加载功能

**步骤**:
1. 创建测试词典文件
```bash
adb shell "mkdir -p /sdcard/NeteaseMusicTranslator"
adb push test_dict.json /sdcard/NeteaseMusicTranslator/translations.json
```

2. 测试词典内容 (`test_dict.json`):
```json
{
  "测试文本": "Văn bản thử nghiệm",
  "自定义翻译": "Dịch tùy chỉnh"
}
```

3. 重启网易云音乐
4. 查看 LSPosed 日志

**预期日志**:
```
NeteaseMusicTranslator: 外部词典加载成功: 2 条翻译
```

**判定**: 如果日志显示加载成功，说明外部词典功能正常 ✅

---

### 测试 5: 性能影响评估
**目标**: 确认模块对应用性能的影响

**步骤**:
1. 测试应用启动时间（使用 adb logcat 监控）
2. 测试界面切换流畅度
3. 测试音乐播放是否受影响

**评估标准**:
- 启动时间增加 < 0.5 秒 ✅
- 界面切换无明显卡顿 ✅
- 音乐播放无影响 ✅

**性能测试命令**:
```bash
# 测试启动时间
adb shell am start -W com.netease.cloudmusic/.activity.MainActivity
# 查看 TotalTime 参数
```

**判定**: 如果启动时间增加小于 500ms，说明性能影响可接受 ✅

---

### 测试 6: 应用更新兼容性
**目标**: 验证网易云音乐更新后模块是否仍然有效

**步骤**:
1. 记录当前网易云音乐版本号
2. 更新网易云音乐到最新版本（如果有更新）
3. 启动更新后的应用
4. 检查翻译是否仍然生效

**预期结果**:
- 模块无需重新安装
- 翻译自动对新版本生效

**判定**: 如果更新后翻译仍然正常，说明版本兼容性良好 ✅

---

### 测试 7: 多语言环境测试
**目标**: 验证模块在不同系统语言下的表现

**步骤**:
1. 切换系统语言为英文
2. 启动网易云音乐
3. 观察翻译是否仍然生效

**预期结果**:
- 无论系统语言如何，翻译都应生效
- 模块 Hook 的是 Resources API，不依赖系统语言

**判定**: 如果不同系统语言下翻译都正常，说明语言独立性良好 ✅

---

## 🐛 常见问题排查

### 问题 1: 模块未生效
**症状**: 界面仍然显示中文，没有越南语

**排查步骤**:
1. 检查 LSPosed 模块是否激活
   ```
   LSPosed Manager → 模块 → 网易云音乐翻译（应显示勾选）
   ```

2. 检查作用域是否设置
   ```
   点击模块 → 应用范围 → com.netease.cloudmusic（应显示勾选）
   ```

3. 检查 LSPosed 日志
   ```bash
   adb logcat | grep "NeteaseMusicTranslator"
   ```

4. 完全重启应用
   ```bash
   adb shell am force-stop com.netease.cloudmusic
   adb shell am start -n com.netease.cloudmusic/.activity.MainActivity
   ```

---

### 问题 2: 部分翻译缺失
**症状**: 有些文本翻译了，有些没有

**原因**: 内置词典只包含 50+ 常用词汇

**解决方案**:
1. 识别缺失的中文词汇
2. 创建外部词典文件补充翻译
3. 或者向项目贡献新的翻译词汇

---

### 问题 3: 翻译后界面错位
**症状**: 越南语文本导致布局显示异常

**原因**: 越南语文本长度可能超出原中文

**解决方案**: 
- 这是预期行为，应用自身的布局适应机制会处理
- 如果严重影响使用，可以简化翻译文本

---

### 问题 4: 日志中看不到模块信息
**症状**: LSPosed 日志中没有 NeteaseMusicTranslator 相关消息

**排查步骤**:
1. 确认 APK 已正确安装
   ```bash
   adb shell pm list packages | grep translator
   # 应显示: package:com.netease.musictranslator
   ```

2. 检查 LSPosed 框架是否正常运行
   ```bash
   adb shell su -c "ls /data/adb/lspd"
   # 应显示 LSPosed 相关文件
   ```

3. 重启设备后重试

---

## 📊 测试报告模板

完成测试后，请填写以下报告：

```
## 测试环境
- 设备型号: _______
- Android 版本: _______
- LSPosed 版本: _______
- 网易云音乐版本: _______
- 模块版本: _______

## 测试结果
- [ ] 测试 1: 模块注入验证
- [ ] 测试 2: 内置翻译验证
- [ ] 测试 3: 未翻译词汇回退
- [ ] 测试 4: 外部词典加载
- [ ] 测试 5: 性能影响评估
- [ ] 测试 6: 应用更新兼容性
- [ ] 测试 7: 多语言环境测试

## 发现的问题
1. _______
2. _______

## 建议改进
1. _______
2. _______
```

---

## 🎯 成功标准

模块被认为测试通过，需要满足：
- ✅ 测试 1-3 全部通过（核心功能）
- ✅ 测试 5 通过（性能可接受）
- ✅ 至少 80% 的常用界面文本正确翻译
- ✅ 无崩溃或严重 Bug

**祝测试顺利！** 🎉
