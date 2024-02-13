package info.local.ridermemory.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TouringRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TouringRecordEntity entity);
    @Update
    void update(TouringRecordEntity entity);
    @Delete
    void delete(TouringRecordEntity entity);

    @Query("SELECT * FROM t_touring_record ORDER BY date DESC")
    LiveData<List<TouringRecordEntity>> findAllTouringRecordsDesc();

    @Query("SELECT * FROM t_touring_record WHERE id = :id")
    LiveData<TouringRecordEntity> findTouringRecordFromId(int id);
}
