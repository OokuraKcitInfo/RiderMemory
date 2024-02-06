package info.local.ridermemory.database;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ExpenseRecordWithCategoryEntity {
    @Embedded public ExpenseRecordEntity expenseRecordEntity;
    @Relation(
            parentColumn = "category_id"
            , entityColumn = "category_id"
    )
    public CategoryEntity categoryEntity;

    @Override
    public String toString() {
        return "ExpenseRecordWithCategoryEntity{" +
                "expenseRecordEntity=" + expenseRecordEntity.toString() +
                ", categoryEntity=" + categoryEntity.toString() +
                '}';
    }
}
