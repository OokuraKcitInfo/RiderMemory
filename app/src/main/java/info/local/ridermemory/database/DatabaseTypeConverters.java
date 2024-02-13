package info.local.ridermemory.database;

import android.net.Uri;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

@ProvidedTypeConverter
public class DatabaseTypeConverters {
    @TypeConverter
    public static Uri stringToUri(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String UriToString(Uri value) {
        return value == null ? null : value.toString();
    }
}
