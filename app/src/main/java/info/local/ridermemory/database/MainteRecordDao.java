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
public interface MainteRecordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MainteRecordEntity entity);
    @Update
    void update(MainteRecordEntity entity);
    @Delete
    void delete(MainteRecordEntity entity);

    @Query("SELECT * FROM t_maintenance_record ORDER BY date DESC, id ASC")
    LiveData<List<MainteRecordEntity>> findAllMaintenanceRecordDesc();

    @Query("SELECT * FROM t_maintenance_record WHERE id = :id")
    LiveData<MainteRecordEntity> findMaintenanceRecord(int id);
}
