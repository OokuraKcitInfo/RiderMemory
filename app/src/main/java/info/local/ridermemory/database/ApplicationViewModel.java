package info.local.ridermemory.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {
    private final ApplicationRepository repository;

    public ApplicationViewModel(@NonNull Application application) throws JSONException, IOException {
        super(application);
        repository = new ApplicationRepository(application);
    }

    public LiveData<List<CategoryEntity>> getCategoryListAsc() { return repository.getCategoryListAsc(); }

    public LiveData<List<ExpenseRecordWithCategoryEntity>> getExpenseRecordListDesc() { return repository.getExpenseRecordListDesc(); }
    public void insertExpenseRecord(ExpenseRecordEntity entity) { repository.insertExpenseRecord(entity); }
    public void updateExpenseRecord(ExpenseRecordEntity entity) { repository.updateExpenseRecord(entity); }
}
