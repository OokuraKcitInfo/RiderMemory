package info.local.ridermemory.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ApplicationRepository {
    private final CategoryDao categoryDao;
    private final ExpenseRecordDao expenseRecordDao;
    private final ExpenseRecordWithCategoryDao expenseRecordWithCategoryDao;

    public ApplicationRepository(Application application) throws JSONException, IOException {
        ApplicationRoomDatabase database = ApplicationRoomDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        expenseRecordDao = database.expenseRecordDao();
        expenseRecordWithCategoryDao = database.expenseRecordWithCategoryDao();
    }

    public LiveData<List<CategoryEntity>> getCategoryListAsc() { return categoryDao.findCategoryListAsc(); }

    public LiveData<List<ExpenseRecordWithCategoryEntity>> getExpenseRecordListDesc() { return expenseRecordWithCategoryDao.findExpenseRecordWithCategoryList(); }
    public void insertExpenseRecord(ExpenseRecordEntity entity) { ApplicationRoomDatabase.databaseWriteExecutor.execute(() -> expenseRecordDao.insert(entity)); }

    public void updateExpenseRecord(ExpenseRecordEntity entity) { ApplicationRoomDatabase.databaseWriteExecutor.execute(() -> expenseRecordDao.update(entity));}
}
