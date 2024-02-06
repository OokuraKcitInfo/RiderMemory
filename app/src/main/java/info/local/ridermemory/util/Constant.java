package info.local.ridermemory.util;

import android.content.Context;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.QuickRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import info.local.ridermemory.R;
import info.local.ridermemory.database.CategoryEntity;

public class Constant {
    public static final String LOG_TAG = "RiderMemory";
    public static final String DATABASE_NAME = "SpinnerRoomSample";
    public static final String CATEGORY_JSON_FILE_NAME = "CategoryData.json";
    public static final CategoryEntity ALL_CATEGORY = new CategoryEntity(0, "すべて", 0);
    public static final CategoryEntity GASOLINE_CATEGORY = new CategoryEntity(0, "ガソリン", 0);
    // データベース保存用日付フォーマット
    public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    // 表示用日付フォーマット
    public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy年M月d日");
    public static final QuickRule<EditText> DATE_INPUT_RULE = new QuickRule<EditText>() {
        @Override
        public boolean isValid(EditText view) {
            String inputText = view.getText().toString();
            try {
                Constant.DISPLAY_DATE_FORMAT.parse(inputText);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }

        @Override
        public String getMessage(Context context) {
            return context.getResources().getString(R.string.validate_date_format_message);
        }
    };
}
