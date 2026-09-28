package com.netease.musictranslator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 配置界面 - 显示模块状态和管理翻译词典
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 简单的文本界面
        TextView textView = new TextView(this);
        textView.setPadding(50, 50, 50, 50);
        textView.setText(getModuleInfo());
        textView.setTextSize(16);

        setContentView(textView);
    }

    private String getModuleInfo() {
        StringBuilder info = new StringBuilder();
        info.append("网易云音乐翻译模块\n\n");
        info.append("版本: 1.0\n");
        info.append("目标应用: 网易云音乐\n\n");
        info.append("使用说明:\n");
        info.append("1. 在 LSPosed 或 Xposed 中激活本模块\n");
        info.append("2. 勾选作用域：com.netease.cloudmusic\n");
        info.append("3. 重启网易云音乐应用\n");
        info.append("4. 中文界面将自动翻译为越南语\n\n");
        info.append("功能特性:\n");
        info.append("• 实时翻译界面文本\n");
        info.append("• 不修改 APK，绕过签名验证\n");
        info.append("• 应用更新后自动生效\n");
        info.append("• 内置 50+ 常用词汇\n\n");
        info.append("注意事项:\n");
        info.append("• 需要 Root 权限\n");
        info.append("• 需要安装 LSPosed 或 Xposed 框架\n");
        info.append("• 首次使用需重启网易云音乐\n");

        return info.toString();
    }
}
