package info.local.ridermemory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;


@Dao
public interface ExpenseRecordWithCategoryDao {
    @Transaction
    @Query("SELECT * FROM t_expense_record")
    LiveData<List<ExpenseRecordWithCategoryEntity>> findExpenseRecordWithCategoryList();
}
