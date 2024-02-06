package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.local.ridermemory.R;

public class TouringRecordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touring_record_list);

        Button backButton = findViewById(R.id.trlBackButton);
        Button addButton = findViewById(R.id.trlAddButton);

        backButton.setOnClickListener(v -> finish());
        addButton.setOnClickListener(v -> {
            Intent it = new Intent(TouringRecordListActivity.this, TouringRecordEditActivity.class);
            startActivity(it);
        });
    }
}