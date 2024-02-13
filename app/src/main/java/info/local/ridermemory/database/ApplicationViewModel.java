package info.local.ridermemory.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {
    private final ApplicationRepository repository;
    private final MutableLiveData<Integer> categoryIdCondition;
    private final LiveData<List<ExpenseRecordWithCategoryEntity>> expenseRecordList;
    private final LiveData<ExpenseRecordTotallingEntity> expenseRecordTotalling;

    public ApplicationViewModel(@NonNull Application application) throws JSONException, IOException {
        super(application);
        repository = new ApplicationRepository(application);
        categoryIdCondition = new MutableLiveData<>();
        expenseRecordList = Transformations.switchMap(categoryIdCondition, categoryId -> {
            if (categoryId == 0) return repository.getExpenseRecordListDesc();
            return repository.getExpenseRecordWithCategoryListByCategoryId(categoryId);
        });
        expenseRecordTotalling = Transformations.switchMap(categoryIdCondition, categoryId -> {
            if (categoryId == 0) return repository.getExpenseRecordTotalling();
            return repository.getExpenseRecordTotallingByCategoryId(categoryId);
        });
    }

    public MutableLiveData<Integer> getCategoryIdCondition() { return categoryIdCondition; }
    public LiveData<List<CategoryEntity>> getCategoryListAsc() { return repository.getCategoryListAsc(); }

    public LiveData<List<ExpenseRecordWithCategoryEntity>> getExpenseRecordListDesc() { return expenseRecordList; }
    public void insertExpenseRecord(ExpenseRecordEntity entity) { repository.insertExpenseRecord(entity); }
    public void updateExpenseRecord(ExpenseRecordEntity entity) { repository.updateExpenseRecord(entity); }
    public LiveData<ExpenseRecordTotallingEntity> getExpenseRecordTotalling() { return expenseRecordTotalling; }
    public void insertTouringRecord(TouringRecordEntity entity) { repository.insertTouringRecord(entity); }
    public void updateTouringRecord(TouringRecordEntity entity) { repository.updateTouringRecord(entity); }
    public LiveData<List<TouringRecordEntity>> getAllTouringRecordsDesc() { return repository.getAllTouringRecordsDesc(); }
    public LiveData<TouringRecordEntity> getTouringRecordFromId(int id) { return repository.getTouringRecordFromId(id); }
    public void insertMaintenanceRecord(MainteRecordEntity entity) { repository.insertMaintenanceRecord(entity); }
    public void updateMaintenanceRecord(MainteRecordEntity entity) { repository.updateMaintenanceRecord(entity); }
    public void deleteMaintenanceRecord(MainteRecordEntity entity) { repository.deleteMaintenanceRecord(entity); }
    public LiveData<List<MainteRecordEntity>> getAllMaintenanceRecordDesc() { return repository.getAllMaintenanceRecordDesc(); }
    public LiveData<MainteRecordEntity> getMaintenanceRecord(int id) { return repository.getMaintenanceRecord(id); }
}
