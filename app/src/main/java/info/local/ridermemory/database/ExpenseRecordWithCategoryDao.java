package info.local.ridermemory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;


@Dao
public interface ExpenseRecordWithCategoryDao {
    @Transaction
    @Query("SELECT * FROM t_expense_record ORDER BY record_date DESC")
    LiveData<List<ExpenseRecordWithCategoryEntity>> findExpenseRecordWithCategoryList();
    @Transaction
    @Query("SELECT * FROM t_expense_record WHERE category_id = :categoryId ORDER BY record_date DESC")
    LiveData<List<ExpenseRecordWithCategoryEntity>> findExpenseRecordWithCategoryListByCategoryId(int categoryId);
}
