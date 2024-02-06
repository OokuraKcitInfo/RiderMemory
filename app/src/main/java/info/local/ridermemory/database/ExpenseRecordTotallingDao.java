package info.local.ridermemory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface ExpenseRecordTotallingDao {
    @Query("SELECT COUNT(*) AS counts, SUM(expense_amount) AS totalAmount FROM t_expense_record")
    LiveData<ExpenseRecordTotallingEntity> findExpenseRecordTotalling();
    @Query("SELECT COUNT(*) AS counts, SUM(expense_amount) AS totalAmount FROM t_expense_record WHERE category_id = :categoryId")
    LiveData<ExpenseRecordTotallingEntity> findExpenseRecordTotallingByCategoryId(int categoryId);
}
