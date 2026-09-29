@echo off
REM 网易云音乐翻译模块 - Windows 自动化测试脚本
chcp 65001 > nul
echo ==========================================
echo 网易云音乐翻译模块 - 测试脚本
echo ==========================================
echo.

REM 检查设备连接
echo 📱 步骤 1: 检查设备连接...
adb devices | findstr "device" > nul
if %errorlevel% neq 0 (
    echo ❌ 错误: 未检测到 Android 设备
    echo 请确保:
    echo   1. 设备已通过 USB 连接
    echo   2. 已启用 USB 调试
    echo   3. 已授权此电脑进行调试
    pause
    exit /b 1
)
echo ✅ 设备已连接
echo.

REM 检查 APK 是否存在
echo 📦 步骤 2: 检查 APK 文件...
set APK_PATH=app-debug.apk
if not exist "%APK_PATH%" (
    echo ❌ 错误: 未找到 %APK_PATH%
    echo 请从 GitHub Actions 下载 APK 并放置在当前目录
    echo 下载地址: https://github.com/bhyz114/NeteaseMusicTranslator/actions
    pause
    exit /b 1
)
echo ✅ 找到 APK 文件
echo.

REM 安装 APK
echo 🔧 步骤 3: 安装模块 APK...
adb install -r "%APK_PATH%"
if %errorlevel% neq 0 (
    echo ❌ 安装失败
    pause
    exit /b 1
)
echo ✅ 模块安装成功
echo.

REM 验证安装
echo 🔍 步骤 4: 验证模块已安装...
adb shell pm list packages | findstr "com.netease.musictranslator" > nul
if %errorlevel% neq 0 (
    echo ❌ 模块未正确安装
    pause
    exit /b 1
)
echo ✅ 模块已安装: com.netease.musictranslator
echo.

REM 推送外部词典（可选）
echo 📝 步骤 5: 推送测试词典...
adb shell mkdir -p /sdcard/NeteaseMusicTranslator
adb push test_dict.json /sdcard/NeteaseMusicTranslator/translations.json
if %errorlevel% equ 0 (
    echo ✅ 测试词典已推送
) else (
    echo ⚠️  测试词典推送失败（可选功能，不影响核心测试）
)
echo.

REM 检查网易云音乐是否安装
echo 🎵 步骤 6: 检查网易云音乐...
adb shell pm list packages | findstr "com.netease.cloudmusic" > nul
if %errorlevel% neq 0 (
    echo ⚠️  警告: 未检测到网易云音乐
    echo 请手动安装网易云音乐后继续测试
) else (
    echo ✅ 网易云音乐已安装
    echo.
    echo 🔄 步骤 7: 重启网易云音乐...
    adb shell am force-stop com.netease.cloudmusic
    timeout /t 2 /nobreak > nul
    adb shell am start -n com.netease.cloudmusic/.activity.MainActivity
    echo ✅ 网易云音乐已启动
)

echo.
echo ==========================================
echo ✅ 自动化测试准备完成！
echo ==========================================
echo.
echo 📋 下一步操作:
echo 1. 打开 LSPosed Manager
echo 2. 进入【模块】页面，激活【网易云音乐翻译】
echo 3. 点击模块名称，设置作用域为: com.netease.cloudmusic
echo 4. 重启网易云音乐
echo 5. 查看界面是否显示越南语
echo.
echo 🔍 查看日志命令:
echo   adb logcat ^| findstr NeteaseMusicTranslator
echo.
echo 📖 详细测试指南: TEST_GUIDE.md
echo.
pause
