package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.local.ridermemory.R;

public class ExpenseRecordListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_record_list);

        Button backButton = findViewById(R.id.erlBackButton);
        Button addButton = findViewById(R.id.erlAddButton);

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

            it = new Intent(ExpenseRecordListActivity.this, ExpenseRecordEditActivity.class);

            startActivity(it);
        }
    }
}