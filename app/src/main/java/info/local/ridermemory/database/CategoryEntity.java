package info.local.ridermemory.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "m_category")
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private int categoryId;
    @ColumnInfo(name = "category_name")
    private String categoryName;
    @ColumnInfo(name = "sort_order")
    private int sortOrder;

    @Ignore
    public CategoryEntity() { }

    public CategoryEntity(int categoryId, String categoryName, int sortOrder) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.sortOrder = sortOrder;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
//        return "CategoryEntity{" +
//                "categoryId=" + categoryId +
//                ", categoryName='" + categoryName + '\'' +
//                ", sortOrder=" + sortOrder +
//                '}';
        return getCategoryName();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CategoryEntity)) return false;
        CategoryEntity target = (CategoryEntity)obj;
        return (getCategoryId() == target.getCategoryId() || getCategoryName().equals(target.getCategoryName()));
    }
}
