package info.local.ridermemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.ParseException;
import java.util.List;

import info.local.ridermemory.R;
import info.local.ridermemory.database.ApplicationViewModel;
import info.local.ridermemory.database.CategoryEntity;
import info.local.ridermemory.database.ExpenseRecordEntity;
import info.local.ridermemory.util.CategorySpinnerAdapter;
import info.local.ridermemory.util.Constant;

public class ExpenseRecordEditActivity extends AppCompatActivity {
    public static final String ENTITY_KEY = "entityKey";
    private EditText dateText;
    private Spinner categorySpinner;
    @NotEmpty(messageResId = R.string.validate_required_message)
    @Digits(integer = 9, messageResId = R.string.validate_digits_message)
    private EditText expenseAmountText;
    private CategorySpinnerAdapter adapter;
    private ApplicationViewModel viewModel;
    private ExpenseRecordEntity entity;
    private Validator validator;
    private boolean isValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_record_edit);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        initializedComponent();
        createCategorySpinnerList();
        bindComponentEvents();
        isValidated = false;
        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                isValidated = true;
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                isValidated = false;
                for (ValidationError error : errors) {
                    View errorView = error.getView();
                    String errorMessage = error.getCollatedErrorMessage(ExpenseRecordEditActivity.this);
                    if (errorView instanceof EditText) {
                        EditText et = (EditText) errorView;
                        et.setError(errorMessage);
                        et.requestFocus();
                    }
                }
            }
        });
        validator.put(dateText, Constant.DATE_INPUT_RULE);
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
                    categoryList.remove(Constant.GASOLINE_CATEGORY);
                    adapter = new CategorySpinnerAdapter(
                            this
                            , android.R.layout.simple_spinner_item
                            , categoryList
                    );

                    categorySpinner.setAdapter(adapter);

                    setTextValueFromEntity();
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
            validator.validate();
            if (!isValidated) return;
            try {
                setEntity();
                if (entity.getExpenseRecordId() != 0) {
                    viewModel.updateExpenseRecord(entity);
                } else {
                    viewModel.insertExpenseRecord(entity);
                }
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

    private void setTextValueFromEntity() {
        Intent intent = getIntent();
        ExpenseRecordEntity extraEntity = (ExpenseRecordEntity) intent.getSerializableExtra(ENTITY_KEY);
        if (extraEntity == null) return;
        entity = extraEntity;
        dateText.setText(entity.getRecordDate());

        int index;
        for (index = 0; index < adapter.getCount(); index++) {
            CategoryEntity distEntity =  (CategoryEntity)adapter.getItem(index);
            CategoryEntity srcEntity = new CategoryEntity();
            srcEntity.setCategoryId(entity.getCategoryId());
            if (distEntity.equals(srcEntity)) break;
        }

        categorySpinner.setSelection(index);
        expenseAmountText.setText(entity.getExpenseAmount() + "");
    }
}