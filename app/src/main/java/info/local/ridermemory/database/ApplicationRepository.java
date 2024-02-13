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
    private final ExpenseRecordTotallingDao expenseRecordTotallingDao;
    
    private final TouringRecordDao touringRecordDao;

    public ApplicationRepository(Application application) throws JSONException, IOException {
        ApplicationRoomDatabase database = ApplicationRoomDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        expenseRecordDao = database.expenseRecordDao();
        expenseRecordWithCategoryDao = database.expenseRecordWithCategoryDao();
        expenseRecordTotallingDao = database.expenseRecordTotallingDao();
        touringRecordDao = database.touringRecordDao();
    }

    public LiveData<List<CategoryEntity>> getCategoryListAsc() { return categoryDao.findCategoryListAsc(); }
    public LiveData<List<ExpenseRecordWithCategoryEntity>> getExpenseRecordListDesc() { return expenseRecordWithCategoryDao.findExpenseRecordWithCategoryList(); }
    public LiveData<List<ExpenseRecordWithCategoryEntity>> getExpenseRecordWithCategoryListByCategoryId(int categoryId) { return expenseRecordWithCategoryDao.findExpenseRecordWithCategoryListByCategoryId(categoryId); }
    public void insertExpenseRecord(ExpenseRecordEntity entity) { ApplicationRoomDatabase.databaseWriteExecutor.execute(() -> expenseRecordDao.insert(entity)); }
    public void updateExpenseRecord(ExpenseRecordEntity entity) { ApplicationRoomDatabase.databaseWriteExecutor.execute(() -> expenseRecordDao.update(entity));}
    public LiveData<ExpenseRecordTotallingEntity> getExpenseRecordTotalling() { return expenseRecordTotallingDao.findExpenseRecordTotalling(); }
    public LiveData<ExpenseRecordTotallingEntity> getExpenseRecordTotallingByCategoryId(int categoryId) { return expenseRecordTotallingDao.findExpenseRecordTotallingByCategoryId(categoryId); }

    public void insertTouringRecord(TouringRecordEntity entity) { ApplicationRoomDatabase.databaseWriteExecutor.execute(() -> touringRecordDao.insert(entity)); }
    public void updateTouringRecord(TouringRecordEntity entity) { ApplicationRoomDatabase.databaseWriteExecutor.execute(() -> touringRecordDao.update(entity)); }
    public LiveData<List<TouringRecordEntity>> getAllTouringRecordsDesc() { return touringRecordDao.findAllTouringRecordsDesc(); }
    public LiveData<TouringRecordEntity> getTouringRecordFromId(int id) { return touringRecordDao.findTouringRecordFromId(id); }
}
