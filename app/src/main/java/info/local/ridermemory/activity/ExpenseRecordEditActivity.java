package info.local.ridermemory.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;

import info.local.ridermemory.R;
import info.local.ridermemory.database.ApplicationViewModel;
import info.local.ridermemory.database.CategoryEntity;
import info.local.ridermemory.database.ExpenseRecordEntity;
import info.local.ridermemory.util.CategorySpinnerAdapter;
import info.local.ridermemory.util.Constant;

public class ExpenseRecordEditActivity extends AppCompatActivity {
    private EditText dateText;
    private Spinner categorySpinner;
    private EditText expenseAmountText;
    private ApplicationViewModel viewModel;
    private ExpenseRecordEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_record_edit);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        initializedComponent();
        createCategorySpinnerList();
        bindComponentEvents();
    }

    private void initializedComponent() {
        entity = new ExpenseRecordEntity();
        dateText = findViewById(R.id.ereDateEditText);
        categorySpinner = findViewById(R.id.ereItemSpinner);
        expenseAmountText = findViewById(R.id.ereMoneyEditText);
    }

    private void createCategorySpinnerList() {
        viewModel
                .getCategoryListAsc()
                .observe(this, categoryList -> {
                    categoryList.remove(new CategoryEntity(0, "ガソリン", 0));
                    CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(
                            this
                            , android.R.layout.simple_spinner_item
                            , categoryList
                    );

                    categorySpinner.setAdapter(adapter);
                });
    }

    private void bindComponentEvents() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryEntity selectedEntity = (CategoryEntity)categorySpinner.getSelectedItem();
                // Todo: 選択された期間と一緒にデータの表示条件を postValue する
                String formattedString = String.format("id: %d, name: %s", selectedEntity.getCategoryId(), selectedEntity.getCategoryName());
                Log.d(Constant.LOG_TAG, formattedString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.ereBackButton).setOnClickListener(view -> finish());
        findViewById(R.id.ereSaveButton).setOnClickListener(view -> {
            try {
                setEntity();
                viewModel.insertExpenseRecord(entity);
                finish();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setEntity() throws ParseException {
        entity.setRecordDate(dateText.getText().toString());
        entity.setCategoryId(((CategoryEntity)categorySpinner.getSelectedItem()).getCategoryId());
        entity.setExpenseAmount(Integer.valueOf(expenseAmountText.getText().toString()));
    }
}