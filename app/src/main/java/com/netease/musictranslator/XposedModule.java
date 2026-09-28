package com.netease.musictranslator;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 网易云音乐翻译模块 - Xposed 核心
 * 通过 Hook Resources 方法实现运行时字符串替换
 */
public class XposedModule implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.netease.cloudmusic";
    private static final String TAG = "NeteaseMusicTranslator";

    // 翻译词典管理器
    private TranslationDictionary dictionary;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 只 Hook 网易云音乐应用
        if (!lpparam.packageName.equals(TARGET_PACKAGE)) {
            return;
        }

        XposedBridge.log(TAG + ": 开始注入网易云音乐");

        // 初始化并加载翻译词典
        dictionary = new TranslationDictionary();
        dictionary.load();

        // Hook Resources.getString(int id)
        hookGetString(lpparam);

        // Hook Resources.getText(int id)
        hookGetText(lpparam);

        // Hook Resources.getString(int id, Object... formatArgs)
        hookGetStringWithArgs(lpparam);

        XposedBridge.log(TAG + ": 注入完成，已加载 " + dictionary.size() + " 条翻译");
    }

    /**
     * Hook Resources.getString(int id)
     */
    private void hookGetString(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                Resources.class,
                "getString",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String original = (String) param.getResult();
                        if (original == null) {
                            return;
                        }

                        String translated = dictionary.translate(original);
                        if (translated != null) {
                            param.setResult(translated);
                            XposedBridge.log(TAG + ": [翻译] " + original + " → " + translated);
                        }
                    }
                }
        );
    }

    /**
     * Hook Resources.getText(int id)
     */
    private void hookGetText(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                Resources.class,
                "getText",
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        CharSequence original = (CharSequence) param.getResult();
                        if (original == null) {
                            return;
                        }

                        String originalStr = original.toString();
                        String translated = dictionary.translate(originalStr);
                        if (translated != null) {
                            param.setResult(translated);
                        }
                    }
                }
        );
    }

    /**
     * Hook Resources.getString(int id, Object... formatArgs)
     * 处理带格式化参数的字符串
     */
    private void hookGetStringWithArgs(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod(
                Resources.class,
                "getString",
                int.class,
                Object[].class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String original = (String) param.getResult();
                        if (original == null) {
                            return;
                        }

                        // 对于格式化字符串，需要更智能的处理
                        // 这里简化处理：直接查找是否有匹配的翻译模板
                        String translated = dictionary.translate(original);
                        if (translated != null) {
                            param.setResult(translated);
                        }
                    }
                }
        );
    }
}
