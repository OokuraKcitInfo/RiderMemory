package info.local.ridermemory.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.mobsandgeeks.saripaar.QuickRule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import info.local.ridermemory.R;
import info.local.ridermemory.database.CategoryEntity;

public class Constant {
    public static final String LOG_TAG = "RiderMemory";
    public static final String PREFS_KEY = "CachePrefsKey";
    public static final String PREFS_CACHE_IMAGE_KEY = "CacheImageKey";
    public static final String DATABASE_NAME = "SpinnerRoomSample";
    public static final String CATEGORY_JSON_FILE_NAME = "CategoryData.json";
    public static final CategoryEntity ALL_CATEGORY = new CategoryEntity(0, "すべて", 0);
    public static final CategoryEntity GASOLINE_CATEGORY = new CategoryEntity(0, "ガソリン", 0);
    public static final String DATE_PICKED_FORMAT = "%d-%d-%d";
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

    public static Bitmap modificationRotate(Bitmap ordinal, int degree) {
        if (degree == 0) return ordinal;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap copiedBitmap = ordinal.copy(Bitmap.Config.ARGB_8888, false);
        return Bitmap.createBitmap(copiedBitmap, 0, 0, copiedBitmap.getWidth(), copiedBitmap.getHeight(), matrix, true);
    }
    public static String writeExternalStorage(@NonNull Context context) {
        if (!Constant.isExternalStorageWritable()) return null;
        SharedPreferences prefs = context.getSharedPreferences(Constant.PREFS_KEY, Context.MODE_PRIVATE);
        String imageString = prefs.getString(Constant.PREFS_CACHE_IMAGE_KEY, "");
        if ("".equals(imageString)) return null;
        byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
        Calendar now = Calendar.getInstance();
        String fileName = now.getTime().getTime() + ".bmp";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
            fos.flush();

            // 保存したファイルをギャラリーへすぐに表示させる
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/bmp");
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DATE_ADDED, now.getTime().getTime() / 1000);
            values.put("_data", file.getAbsolutePath());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
            return null;
        }
    }

    public static Bitmap readImageFromExternalStorage(File file) {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        if (!Constant.isExternalStorageReadable()) return bitmap;
        try (InputStream is = new FileInputStream(file)) {
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
        }
        return bitmap;
    }

    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public static void writeStringInSharedPrefs(@NonNull Context context, @NonNull String prefsDataKey, @NonNull String dataString) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefsDataKey, dataString);
        editor.apply();
    }
}
