package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.local.ridermemory.R;

public class RefuelRecordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_record_list);

        Button backButton = findViewById(R.id.rrlBackButton);
        Button addButton = findViewById(R.id.rrlAddButton);

        backButton.setOnClickListener(new BackButtonEvent());
        addButton.setOnClickListener(new AddButtonEvent());
    }
    class BackButtonEvent implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            finish();
        }
    }
    class AddButtonEvent implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            Intent it = new Intent();

            it = new Intent(RefuelRecordListActivity.this, RefuelRecordEditActivity.class);

            startActivity(it);
        }
    }
}