package info.local.ridermemory.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.local.ridermemory.R;
import info.local.ridermemory.database.ApplicationViewModel;
import info.local.ridermemory.database.MainteRecordEntity;
import info.local.ridermemory.util.Constant;

public class MainteRecordEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String ENTITY_ID_KEY = "entity_id";
    private EditText dateEdit;
    @Digits(integer = 9, messageResId = R.string.validate_decimal_message)
    private EditText mileageEdit;
    @NotEmpty(messageResId = R.string.validate_required_message)
    private EditText mainteEdit;
    private EditText nextMainteEdit;
    private ApplicationViewModel viewModel;
    private MainteRecordEntity entity;
    private Validator validator;
    private boolean isValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainte_record_edit);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        int id = getIntent().getIntExtra(ENTITY_ID_KEY, 0);

        viewModel.getMaintenanceRecord(id).observe(this, mainteRecordEntity -> {
            if (mainteRecordEntity == null) {
                entity = new MainteRecordEntity();
                return;
            }
            entity = mainteRecordEntity;
            setComponentFromEntity();
            Log.d(Constant.LOG_TAG, entity.toString());
        });

        initializedComponent();
        bindComponent();

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
                    String errorMessage = error.getCollatedErrorMessage(MainteRecordEditActivity.this);
                    if (errorView instanceof EditText) {
                        EditText et = (EditText) errorView;
                        et.setError(errorMessage);
                        et.requestFocus();
                    }
                }
            }
        });
        validator.put(dateEdit, Constant.DATE_INPUT_RULE);
    }

    private void initializedComponent() {
        dateEdit = findViewById(R.id.mreDateEditText);
        mileageEdit = findViewById(R.id.mreMileageEditText);
        mainteEdit = findViewById(R.id.mreMainteEditText);
        nextMainteEdit = findViewById(R.id.mreNextMainteEditText);
    }

    private void bindComponent() {
        dateEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
            (new MainteRecordEditActivity.DatePickerFragment()).show(getSupportFragmentManager(), "datePicker");
            return false;
        });
        findViewById(R.id.mreBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.mreSaveButton).setOnClickListener(v -> {
            validator.validate();
            if (!isValidated) return;
            setEntityFromInput();
            if (entity.getMaintenanceId() == 0) {
                viewModel.insertMaintenanceRecord(entity);
            } else {
                viewModel.updateMaintenanceRecord(entity);
            }
            finish();
        });
    }

    private void setComponentFromEntity() {
        try {
            dateEdit.setText(Constant.DISPLAY_DATE_FORMAT.format(Constant.DB_DATE_FORMAT.parse(entity.getMaintenanceDate())));
            mileageEdit.setText(entity.getMileageValue() + "");
            mainteEdit.setText(entity.getMaintenanceContent());
            nextMainteEdit.setText(entity.getNextMaintenanceContent());
        } catch (ParseException e) {
            Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
        }
    }

    private void setEntityFromInput() {
        entity.setMileageValue(Double.parseDouble(mileageEdit.getText().toString()));
        entity.setMaintenanceContent(mainteEdit.getText().toString());
        entity.setNextMaintenanceContent(nextMainteEdit.getText().toString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            Date selectedDate = Constant.DB_DATE_FORMAT.parse(String.format(Constant.DATE_PICKED_FORMAT, year, month + 1, dayOfMonth));
            entity.setMaintenanceDate (Constant.DB_DATE_FORMAT.format(selectedDate));
            dateEdit.setText(Constant.DISPLAY_DATE_FORMAT.format(selectedDate));
        } catch (ParseException e) {
            Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            try {
                MainteRecordEntity entity = ((MainteRecordEditActivity)getActivity()).entity;
                if (entity.getMaintenanceId() != 0) {
                    c.setTime(Constant.DB_DATE_FORMAT.parse(entity.getMaintenanceDate()));
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DATE);
                }
            } catch (ParseException e) {
                Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
                throw new RuntimeException(e);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (MainteRecordEditActivity)getActivity(), year, month, day);
        }
    }
}
