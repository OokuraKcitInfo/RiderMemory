package info.local.ridermemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.List;

import info.local.ridermemory.R;
import info.local.ridermemory.database.ApplicationViewModel;
import info.local.ridermemory.database.MainteRecordEntity;
import info.local.ridermemory.util.Constant;

public class MainteRecordListActivity extends AppCompatActivity {
    private ApplicationViewModel viewModel;
    private RecyclerView maintenanceRecordListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainte_record_list);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        initializedComponent();
        bindComponent();
    }

    private void initializedComponent() {
        maintenanceRecordListRecyclerView = findViewById(R.id.mrlMainteDetailList);
        viewModel.getAllMaintenanceRecordDesc().observe(this, maintenanceRecordEntities -> {
            MaintenanceRecordListRecyclerViewAdapter adapter = new MaintenanceRecordListRecyclerViewAdapter(maintenanceRecordEntities);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            maintenanceRecordListRecyclerView.setHasFixedSize(true);
            maintenanceRecordListRecyclerView.setLayoutManager(manager);
            maintenanceRecordListRecyclerView.setAdapter(adapter);
        });
    }

    private void bindComponent() {
        findViewById(R.id.mrlBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.mrlAddButton).setOnClickListener(v -> startActivity(new Intent(MainteRecordListActivity.this, MainteRecordEditActivity.class)));
    }

    private class MaintenanceRecordListRecyclerViewAdapter extends RecyclerView.Adapter<MaintenanceRecordListRecyclerViewAdapter.MaintenanceRecordListRecyclerViewHolder> {
        private List<MainteRecordEntity> maintenanceRecordEntityList;

        public MaintenanceRecordListRecyclerViewAdapter(List<MainteRecordEntity> maintenanceRecordEntityList) {
            this.maintenanceRecordEntityList = maintenanceRecordEntityList;
        }

        @NonNull
        @Override
        public MaintenanceRecordListRecyclerViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mainte_recode_list_cell, parent, false);
            return new MaintenanceRecordListRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MaintenanceRecordListRecyclerViewHolder holder, int position) {
            holder.setEntity(maintenanceRecordEntityList.get(position));
        }

        @Override
        public int getItemCount() {
            return maintenanceRecordEntityList.size();
        }

        private class MaintenanceRecordListRecyclerViewHolder extends RecyclerView.ViewHolder {
            private ImageView nextMainteExistIconImageView;
            private TextView dateText;
            private TextView mileageText;
            private TextView mainteText;
            private MainteRecordEntity entity;

            public MaintenanceRecordListRecyclerViewHolder(@NonNull View itemView) {
                super(itemView);

                nextMainteExistIconImageView = itemView.findViewById(R.id.rmrlcNextMainteImageView);
                dateText = itemView.findViewById(R.id.rmrlcDateTextView);
                mileageText = itemView.findViewById(R.id.rmrlcMileageTextView);
                mainteText = itemView.findViewById(R.id.rmrlcMainteTextView);

                itemView.setOnClickListener(view -> {
                    Intent it = new Intent(MainteRecordListActivity.this, MainteRecordEditActivity.class);
                    it.putExtra(MainteRecordEditActivity.ENTITY_ID_KEY, entity.getMaintenanceId());
                    startActivity(it);
                });
            }

            public void setEntity(MainteRecordEntity entity) {
                try {
                    this.entity = entity;
                    // Todo: 次回メンテナンスに入力があればアイコン表示する処理を実装する
                    if (!"".equals(entity.getNextMaintenanceContent())) {
//                        nextMainteExistIconImageView.setImageIcon();
                    }
                    dateText.setText(Constant.DISPLAY_DATE_FORMAT.format(Constant.DB_DATE_FORMAT.parse(entity.getMaintenanceDate())));
                    mileageText.setText(entity.getMileageValue() + "");
                    mainteText.setText(entity.getMaintenanceContent());
                } catch (ParseException e) {
                    Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
                }
            }
        }
    }
}