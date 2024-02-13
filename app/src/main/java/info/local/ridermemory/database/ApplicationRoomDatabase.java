package info.local.ridermemory.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.local.ridermemory.util.Constant;

@Database(entities = {CategoryEntity.class, ExpenseRecordEntity.class, TouringRecordEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DatabaseTypeConverters.class})
public abstract class ApplicationRoomDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract ExpenseRecordDao expenseRecordDao();
    public abstract ExpenseRecordWithCategoryDao expenseRecordWithCategoryDao();
    public abstract ExpenseRecordTotallingDao expenseRecordTotallingDao();
    public abstract TouringRecordDao touringRecordDao();
    private static volatile ApplicationRoomDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static LinkedList<CategoryEntity> initCategoryList;

    static ApplicationRoomDatabase getInstance(final Context context) throws IOException, JSONException {
        if (INSTANCE == null) {
            synchronized (ApplicationRoomDatabase.class) {
                if (INSTANCE == null) {
                    InputStream is = context.getAssets().open(Constant.CATEGORY_JSON_FILE_NAME);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();

                    String readLineString;
                    while ((readLineString = br.readLine()) != null) {
                        sb.append(readLineString);
                    }

                    is.close();
                    br.close();

                    JSONArray jsonArray = new JSONArray(sb.toString());
                    initCategoryList = new LinkedList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CategoryEntity entity = new CategoryEntity();
                        entity.setCategoryName(jsonObject.getString("name"));
                        entity.setSortOrder(jsonObject.getInt("sort_order"));
                        initCategoryList.add(entity);
                    }

                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext()
                                    , ApplicationRoomDatabase.class
                                    , Constant.DATABASE_NAME
                            )
                            .addCallback(ROOM_DATABASE_CALLBACK)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback ROOM_DATABASE_CALLBACK = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CategoryDao dao = INSTANCE.categoryDao();
                for (CategoryEntity entity : initCategoryList) {
                    dao.insert(entity);
                }
            });
        }
    };
}
