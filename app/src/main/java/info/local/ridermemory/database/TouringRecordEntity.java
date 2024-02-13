package info.local.ridermemory.database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "t_touring_record")
public class TouringRecordEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int touringId;
    @ColumnInfo(name = "date")
    private String touringDate;
    @ColumnInfo(name = "place")
    private String touringPlace;
    @ColumnInfo(name = "touring_image_path")
    private Uri touringImagePath;
    @ColumnInfo(name = "memo")
    private String touringMemo;

    @ColumnInfo(name = "touring_image_degree")
    private int touringImageDegree;
    public int getTouringId() {
        return touringId;
    }

    public void setTouringId(int touringId) {
        this.touringId = touringId;
    }

    public String getTouringDate() {
        return touringDate;
    }

    public void setTouringDate(String touringDate) {
        this.touringDate = touringDate;
    }

    public String getTouringPlace() {
        return touringPlace;
    }

    public void setTouringPlace(String touringPlace) {
        this.touringPlace = touringPlace;
    }

    public Uri getTouringImagePath() {
        return touringImagePath;
    }

    public void setTouringImagePath(Uri touringImagePath) {
        this.touringImagePath = touringImagePath;
    }

    public String getTouringMemo() {
        return touringMemo;
    }

    public void setTouringMemo(String touringMemo) {
        this.touringMemo = touringMemo;
    }

    public int getTouringImageDegree() {
        return touringImageDegree;
    }

    public void setTouringImageDegree(int touringImageDegree) {
        this.touringImageDegree = touringImageDegree;
    }
}
