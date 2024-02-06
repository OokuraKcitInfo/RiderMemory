package info.local.ridermemory.util;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

import info.local.ridermemory.database.CategoryEntity;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryEntity> {
    public CategorySpinnerAdapter(@NonNull Context context, int resource, @NonNull List<CategoryEntity> objects) {
        super(context, resource, objects);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
