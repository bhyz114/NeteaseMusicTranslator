#!/bin/bash
# 网易云音乐翻译模块 - 自动化测试脚本

echo "=========================================="
echo "网易云音乐翻译模块 - 测试脚本"
echo "=========================================="
echo ""

# 检查设备连接
echo "📱 步骤 1: 检查设备连接..."
adb devices | grep -v "List" | grep "device" > /dev/null
if [ $? -ne 0 ]; then
    echo "❌ 错误: 未检测到 Android 设备"
    echo "请确保:"
    echo "  1. 设备已通过 USB 连接"
    echo "  2. 已启用 USB 调试"
    echo "  3. 已授权此电脑进行调试"
    exit 1
fi
echo "✅ 设备已连接"
echo ""

# 检查 APK 是否存在
echo "📦 步骤 2: 检查 APK 文件..."
APK_PATH="app-debug.apk"
if [ ! -f "$APK_PATH" ]; then
    echo "❌ 错误: 未找到 $APK_PATH"
    echo "请从 GitHub Actions 下载 APK 并放置在当前目录"
    echo "下载地址: https://github.com/bhyz114/NeteaseMusicTranslator/actions"
    exit 1
fi
echo "✅ 找到 APK 文件"
echo ""

# 安装 APK
echo "🔧 步骤 3: 安装模块 APK..."
adb install -r "$APK_PATH"
if [ $? -ne 0 ]; then
    echo "❌ 安装失败"
    exit 1
fi
echo "✅ 模块安装成功"
echo ""

# 验证安装
echo "🔍 步骤 4: 验证模块已安装..."
adb shell pm list packages | grep "com.netease.musictranslator" > /dev/null
if [ $? -ne 0 ]; then
    echo "❌ 模块未正确安装"
    exit 1
fi
echo "✅ 模块已安装: com.netease.musictranslator"
echo ""

# 推送外部词典（可选）
echo "📝 步骤 5: 推送测试词典..."
adb shell mkdir -p /sdcard/NeteaseMusicTranslator
adb push test_dict.json /sdcard/NeteaseMusicTranslator/translations.json
if [ $? -eq 0 ]; then
    echo "✅ 测试词典已推送"
else
    echo "⚠️  测试词典推送失败（可选功能，不影响核心测试）"
fi
echo ""

# 检查网易云音乐是否安装
echo "🎵 步骤 6: 检查网易云音乐..."
adb shell pm list packages | grep "com.netease.cloudmusic" > /dev/null
if [ $? -ne 0 ]; then
    echo "⚠️  警告: 未检测到网易云音乐"
    echo "请手动安装网易云音乐后继续测试"
else
    echo "✅ 网易云音乐已安装"

    # 重启网易云音乐
    echo ""
    echo "🔄 步骤 7: 重启网易云音乐..."
    adb shell am force-stop com.netease.cloudmusic
    sleep 2
    adb shell am start -n com.netease.cloudmusic/.activity.MainActivity
    echo "✅ 网易云音乐已启动"
fi

echo ""
echo "=========================================="
echo "✅ 自动化测试准备完成！"
echo "=========================================="
echo ""
echo "📋 下一步操作:"
echo "1. 打开 LSPosed Manager"
echo "2. 进入【模块】页面，激活【网易云音乐翻译】"
echo "3. 点击模块名称，设置作用域为: com.netease.cloudmusic"
echo "4. 重启网易云音乐"
echo "5. 查看界面是否显示越南语"
echo ""
echo "🔍 查看日志命令:"
echo "  adb logcat | grep NeteaseMusicTranslator"
echo ""
echo "📖 详细测试指南: TEST_GUIDE.md"
echo ""
