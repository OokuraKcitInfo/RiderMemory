package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.local.ridermemory.R;

public class TouringRecordEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touring_record_edit);

        Button backButton = findViewById(R.id.treBackButton);
        Button saveButton = findViewById(R.id.treSaveButton);

        backButton.setOnClickListener(new BackButtonEvent());
        saveButton.setOnClickListener(new SaveButtonEvent());
    }

    class BackButtonEvent implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            finish();
        }
    }
    class SaveButtonEvent implements View.OnClickListener{

        @Override
        public void onClick(View view) {


        }
    }
}