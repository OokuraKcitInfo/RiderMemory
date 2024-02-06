package info.local.ridermemory.util;

import java.text.SimpleDateFormat;

import info.local.ridermemory.database.CategoryEntity;

public class Constant {
    public static final String LOG_TAG = "RiderMemory";
    public static final String DATABASE_NAME = "SpinnerRoomSample";
    public static final String CATEGORY_JSON_FILE_NAME = "CategoryData.json";
    public static final CategoryEntity ALL_CATEGORY = new CategoryEntity(0, "すべて", 0);
    // データベース保存用日付フォーマット
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    // 表示用日付フォーマット
    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy年M月d日");
}
