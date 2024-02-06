package info.local.ridermemory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpenseRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ExpenseRecordEntity entity);

    @Update
    void update(ExpenseRecordEntity entity);

    @Query("DELETE FROM t_expense_record")
    void deleteAllData();

//    @Query("SELECT t1.id as id, t1.record_date as record_date, t1.expense_amount as expense_amount FROM t_expense_record as t1 INNER JOIN m_category as m1 ON t1.category_id = m1.id ORDER BY t1.record_date DESC")
    @Query("SELECT * FROM t_expense_record ORDER BY record_date ASC")
    LiveData<List<ExpenseRecordEntity>> findExpenseRecordListDesc();

}
