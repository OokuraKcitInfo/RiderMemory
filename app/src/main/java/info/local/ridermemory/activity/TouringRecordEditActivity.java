package info.local.ridermemory.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.local.ridermemory.R;
import info.local.ridermemory.database.ApplicationViewModel;
import info.local.ridermemory.database.TouringRecordEntity;
import info.local.ridermemory.util.Constant;

public class TouringRecordEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String ENTITY_ID_KEY = "entity_id";
    private EditText dateEdit;
    private EditText placeEdit;
    private ImageView imageView;
    private EditText memoEdit;
    private ApplicationViewModel viewModel;
    private TouringRecordEntity entity;
    private Validator validator;
    private boolean isValidated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touring_record_edit);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        int id = getIntent().getIntExtra(ENTITY_ID_KEY, 0);
        viewModel.getTouringRecordFromId(id).observe(this, touringRecordEntity -> {
            if (touringRecordEntity == null) {
                entity = new TouringRecordEntity();
                return;
            }
            entity = touringRecordEntity;
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
                    String errorMessage = error.getCollatedErrorMessage(TouringRecordEditActivity.this);
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
        dateEdit = findViewById(R.id.treDateEditText);
        placeEdit = findViewById(R.id.trePlaceEditText);
        imageView = findViewById(R.id.trePhotoImageView);
        memoEdit = findViewById(R.id.treMemoEditText);
    }

    private void bindComponent() {
        dateEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
            (new DatePickerFragment()).show(getSupportFragmentManager(), "datePicker");
            return false;
        });
        imageView.setOnClickListener(view ->  activityResultLauncher.launch(new String[]{"image/*"}));
        findViewById(R.id.treBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.treSaveButton).setOnClickListener(v -> {
            validator.validate();
            if (!isValidated) return;
            setEntityFromInput();
            if (entity.getTouringId() == 0) {
                viewModel.insertTouringRecord (entity);
            } else {
                viewModel.updateTouringRecord(entity);
            }
            // キャッシュした画像を削除
            Constant.writeStringInSharedPrefs(this, Constant.PREFS_CACHE_IMAGE_KEY, "");
            finish();
        });
    }

    private void setComponentFromEntity() {
        try {
            Uri imageUri = entity.getTouringImagePath();
            if (imageUri != null) {
                Bitmap bitmap = Constant.readImageFromExternalStorage(new File(imageUri.getPath()));
                imageView.setImageBitmap(Constant.modificationRotate(bitmap, entity.getTouringImageDegree()));
            }
            dateEdit.setText(Constant.DISPLAY_DATE_FORMAT.format(Constant.DB_DATE_FORMAT.parse(entity.getTouringDate())));
            placeEdit.setText(entity.getTouringPlace());
            memoEdit.setText(entity.getTouringMemo());
        } catch (ParseException e) {
            Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
        }
    }

    private void setEntityFromInput() {
        String filePath = Constant.writeExternalStorage(this);
        if (filePath != null) entity.setTouringImagePath(Uri.parse(filePath));
        entity.setTouringPlace(placeEdit.getText().toString());
        entity.setTouringMemo(memoEdit.getText().toString());
    }

    @SuppressLint("RestrictedApi")
    private final ActivityResultLauncher<String[]> activityResultLauncher
            = registerForActivityResult(
            // １つのみ選択可能な状態でギャラリーを開く
            new ActivityResultContracts.OpenDocument(),
            // 背景画像を選択するギャラリーから戻ってきた際に呼ばれる処理
            // result に選択された画像のパス（URI）が格納されている
            result -> {
                // 画像が選択されなかったら何もしない
                if (result == null) return;
                double[] latLong;
                try {
                    InputStream stream = getContentResolver().openInputStream(result);
                    if (stream == null) return;
                    ExifInterface exifInterface = new ExifInterface(stream);

                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                    int rotateDegree = exifInterface.getRotationDegrees();
                    imageView.setImageBitmap(Constant.modificationRotate(selectedImage, rotateDegree));
                    entity.setTouringImageDegree(rotateDegree);

                    // 読み込んだ画像は SharedPrefs にキャッシュ
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    String bitmapStr = Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT);
                    Constant.writeStringInSharedPrefs(this, Constant.PREFS_CACHE_IMAGE_KEY, bitmapStr);
                    baos.close();

                    stream.close();
                } catch (Exception e) {
                    Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
                }
            }
    );

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            Date selectedDate = Constant.DB_DATE_FORMAT.parse(String.format(Constant.DATE_PICKED_FORMAT, year, month + 1, dayOfMonth));
            entity.setTouringDate(Constant.DB_DATE_FORMAT.format(selectedDate));
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
                TouringRecordEntity entity = ((TouringRecordEditActivity)getActivity()).entity;
                if (entity.getTouringId() != 0) {
                    c.setTime(Constant.DB_DATE_FORMAT.parse(entity.getTouringDate()));
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DATE);
                }
            } catch (ParseException e) {
                Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
                throw new RuntimeException(e);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (TouringRecordEditActivity)getActivity(), year, month, day);
        }
    }
}