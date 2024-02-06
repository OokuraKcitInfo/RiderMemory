package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.local.ridermemory.R;

public class MainteRecordEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainte_record_edit);

        Button backButton = findViewById(R.id.mreBackButton);

        backButton.setOnClickListener(new BackButtonEvent());
    }
    class BackButtonEvent implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            finish();
        }
    }
}