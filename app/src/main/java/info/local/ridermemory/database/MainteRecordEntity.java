package info.local.ridermemory.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_maintenance_record")
public class MainteRecordEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int maintenanceId;
    @ColumnInfo(name = "date")
    private String maintenanceDate;
    @ColumnInfo(name = "mileage_value")
    private double mileageValue;
    @ColumnInfo(name = "maintenance_content")
    private String maintenanceContent;
    @ColumnInfo(name = "next_maintenance_content")
    private String nextMaintenanceContent;

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public double getMileageValue() {
        return mileageValue;
    }

    public void setMileageValue(double mileageValue) {
        this.mileageValue = mileageValue;
    }

    public String getMaintenanceContent() {
        return maintenanceContent;
    }

    public void setMaintenanceContent(String maintenanceContent) {
        this.maintenanceContent = maintenanceContent;
    }

    public String getNextMaintenanceContent() {
        return nextMaintenanceContent;
    }

    public void setNextMaintenanceContent(String nextMaintenanceContent) {
        this.nextMaintenanceContent = nextMaintenanceContent;
    }
}
