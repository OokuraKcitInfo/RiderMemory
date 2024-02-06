package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.local.ridermemory.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button touringButton = findViewById(R.id.MainTouringRecordButton);
        Button mainteButton = findViewById(R.id.MainMainteRecordButton);
        Button refuelButton = findViewById(R.id.MainRefuelRecordButton);
        Button expenseButton = findViewById(R.id.MainExpenseRecordButton);
        Button vehicleButton = findViewById(R.id.MainVehicleInformationButton);
        Button calendarButton = findViewById(R.id.MainCalendarButton);

        touringButton.setOnClickListener(v -> moveNextActivity(TouringRecordListActivity.class));
        mainteButton.setOnClickListener(v -> moveNextActivity(MainteRecordListActivity.class));
        refuelButton.setOnClickListener(v -> moveNextActivity(RefuelRecordListActivity.class));
        expenseButton.setOnClickListener(v -> moveNextActivity(ExpenseRecordListActivity.class));
        vehicleButton.setOnClickListener(v -> moveNextActivity(VehicleInformationActivity.class));
        calendarButton.setOnClickListener(v -> { });
    }

    private void moveNextActivity(Class<?> cls) {
        Intent it = new Intent(MainActivity.this, cls);
        startActivity(it);
    }
}