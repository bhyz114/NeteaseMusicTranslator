# Xposed module - keep all hook classes
-keep class com.netease.musictranslator.** { *; }
-keepclassmembers class * {
    @de.robv.android.xposed.** *;
}
