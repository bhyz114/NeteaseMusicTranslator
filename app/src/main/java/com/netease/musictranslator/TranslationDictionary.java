package com.netease.musictranslator;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XposedBridge;

/**
 * 翻译词典管理器
 * 支持从外部 JSON 文件加载翻译词典
 */
public class TranslationDictionary {

    private static final String TAG = "NeteaseMusicTranslator";
    private static final String DICT_FILE_NAME = "netease_translations.json";

    private final Map<String, String> translations = new HashMap<>();

    /**
     * 加载翻译词典
     * 优先从外部存储加载，如果不存在则使用内置词典
     */
    public void load() {
        // 先加载内置基础词典
        loadBuiltInTranslations();

        // 尝试从外部存储加载扩展词典
        loadExternalDictionary();

        XposedBridge.log(TAG + ": 词典加载完成，共 " + translations.size() + " 条翻译");
    }

    /**
     * 获取翻译
     */
    public String translate(String original) {
        return translations.get(original);
    }

    /**
     * 获取词典大小
     */
    public int size() {
        return translations.size();
    }

    /**
     * 加载内置基础翻译
     */
    private void loadBuiltInTranslations() {
        // 常用界面文本
        translations.put("下载", "Tải xuống");
        translations.put("播放", "Phát");
        translations.put("暂停", "Tạm dừng");
        translations.put("我的", "Của tôi");
        translations.put("发现", "Khám phá");
        translations.put("搜索", "Tìm kiếm");
        translations.put("歌单", "Danh sách phát");
        translations.put("歌手", "Ca sĩ");
        translations.put("专辑", "Album");
        translations.put("收藏", "Yêu thích");
        translations.put("分享", "Chia sẻ");
        translations.put("评论", "Bình luận");
        translations.put("设置", "Cài đặt");
        translations.put("退出", "Thoát");
        translations.put("登录", "Đăng nhập");
        translations.put("注册", "Đăng ký");
        translations.put("取消", "Hủy");
        translations.put("确定", "Xác nhận");
        translations.put("删除", "Xóa");
        translations.put("编辑", "Chỉnh sửa");

        // 播放控制
        translations.put("添加", "Thêm");
        translations.put("返回", "Quay lại");
        translations.put("下一首", "Bài tiếp theo");
        translations.put("上一首", "Bài trước");
        translations.put("随机播放", "Phát ngẫu nhiên");
        translations.put("单曲循环", "Lặp lại bài hát");
        translations.put("列表循环", "Lặp lại danh sách");
        translations.put("音质", "Chất lượng âm thanh");
        translations.put("歌词", "Lời bài hát");

        // 内容分类
        translations.put("推荐", "Đề xuất");
        translations.put("排行榜", "Bảng xếp hạng");
        translations.put("最新", "Mới nhất");
        translations.put("热门", "Phổ biến");
        translations.put("私人FM", "FM cá nhân");
        translations.put("每日推荐", "Đề xuất hàng ngày");
        translations.put("本地音乐", "Nhạc cục bộ");
        translations.put("云盘", "Cloud");
        translations.put("已下载", "Đã tải xuống");
        translations.put("正在播放", "Đang phát");
        translations.put("播放历史", "Lịch sử phát");
        translations.put("我喜欢的音乐", "Nhạc tôi thích");
        translations.put("创建歌单", "Tạo danh sách phát");

        // 状态提示
        translations.put("全部", "Tất cả");
        translations.put("加载中", "Đang tải");
        translations.put("加载失败", "Tải thất bại");
        translations.put("网络错误", "Lỗi mạng");
        translations.put("请稍候", "Vui lòng đợi");
        translations.put("暂无数据", "Không có dữ liệu");
        translations.put("刷新", "Làm mới");
        translations.put("完成", "Hoàn thành");
        translations.put("保存", "Lưu");
        translations.put("发送", "Gửi");

        XposedBridge.log(TAG + ": 已加载 " + translations.size() + " 条内置翻译");
    }

    /**
     * 从外部存储加载完整词典
     * 文件路径: /sdcard/netease_translations.json
     *
     * JSON 格式:
     * {
     *   "下载": "Tải xuống",
     *   "播放": "Phát",
     *   ...
     * }
     */
    private void loadExternalDictionary() {
        try {
            File externalStorage = Environment.getExternalStorageDirectory();
            File dictFile = new File(externalStorage, DICT_FILE_NAME);

            if (!dictFile.exists()) {
                XposedBridge.log(TAG + ": 外部词典文件不存在: " + dictFile.getAbsolutePath());
                XposedBridge.log(TAG + ": 如需扩展翻译，请将 JSON 词典放置在: " + dictFile.getAbsolutePath());
                return;
            }

            XposedBridge.log(TAG + ": 正在加载外部词典: " + dictFile.getAbsolutePath());

            // 使用 Gson 解析 JSON
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();

            FileInputStream fis = new FileInputStream(dictFile);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");

            Map<String, String> externalDict = gson.fromJson(reader, type);
            reader.close();
            fis.close();

            if (externalDict != null && !externalDict.isEmpty()) {
                // 合并到现有词典（外部词典优先）
                int beforeSize = translations.size();
                translations.putAll(externalDict);
                int newEntries = translations.size() - beforeSize;

                XposedBridge.log(TAG + ": 外部词典加载成功，新增/覆盖 " + newEntries + " 条翻译");
            } else {
                XposedBridge.log(TAG + ": 外部词典文件为空");
            }

        } catch (Exception e) {
            XposedBridge.log(TAG + ": 加载外部词典失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
