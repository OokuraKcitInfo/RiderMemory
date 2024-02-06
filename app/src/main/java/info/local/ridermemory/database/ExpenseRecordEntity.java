package info.local.ridermemory.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "t_expense_record")
public class ExpenseRecordEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int expenseRecordId;
    @ColumnInfo(name = "record_date")
    private String recordDate;
    @ColumnInfo(name = "category_id")
    private int categoryId;
    @ColumnInfo(name = "expense_amount")
    private int expenseAmount;

    @Ignore
    public ExpenseRecordEntity() {}

    public ExpenseRecordEntity(String recordDate, int categoryId, int expenseAmount) {
        this.recordDate = recordDate;
        this.categoryId = categoryId;
        this.expenseAmount = expenseAmount;
    }

    public int getExpenseRecordId() {
        return expenseRecordId;
    }

    public void setExpenseRecordId(int expenseRecordId) {
        this.expenseRecordId = expenseRecordId;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    @Override
    public String toString() {
        return "ExpenseRecordEntity{" +
                "expenseRecordId=" + expenseRecordId +
                ", recordDate='" + recordDate + '\'' +
                ", categoryId=" + categoryId +
                ", expenseAmount=" + expenseAmount +
                '}';
    }
}
