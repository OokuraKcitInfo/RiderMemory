package info.local.ridermemory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CategoryEntity entity);
    @Query("DELETE FROM m_category")
    void deleteAllCategory();
    @Query("SELECT * FROM m_category ORDER BY sort_order ASC")
    LiveData<List<CategoryEntity>> findCategoryListAsc();
}
