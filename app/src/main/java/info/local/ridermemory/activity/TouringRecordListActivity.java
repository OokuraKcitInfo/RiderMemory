package info.local.ridermemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import info.local.ridermemory.database.TouringRecordEntity;
import info.local.ridermemory.util.Constant;

public class TouringRecordListActivity extends AppCompatActivity {
    private ApplicationViewModel viewModel;
    private RecyclerView touringRecordListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touring_record_list);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        initializedComponent();
        bindComponent();
    }

    private void initializedComponent() {
        touringRecordListRecyclerView = findViewById(R.id.trlTouringDetailList);
        viewModel.getAllTouringRecordsDesc().observe(this, touringRecordEntities -> {
            TouringRecordListRecyclerViewAdapter adapter = new TouringRecordListRecyclerViewAdapter(touringRecordEntities);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            touringRecordListRecyclerView.setHasFixedSize(true);
            touringRecordListRecyclerView.setLayoutManager(manager);
            touringRecordListRecyclerView.setAdapter(adapter);
        });
    }

    private void bindComponent() {
        findViewById(R.id.trlBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.trlAddButton).setOnClickListener(v -> startActivity(new Intent(TouringRecordListActivity.this, TouringRecordEditActivity.class)));
    }

    private class TouringRecordListRecyclerViewAdapter extends RecyclerView.Adapter<TouringRecordListRecyclerViewAdapter.TouringRecordListRecyclerViewHolder> {
        private List<TouringRecordEntity> touringRecordEntityList;

        public TouringRecordListRecyclerViewAdapter(List<TouringRecordEntity> touringRecordEntityList) {
            this.touringRecordEntityList = touringRecordEntityList;
        }

        @NonNull
        @Override
        public TouringRecordListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_touring_record_list_cell, parent, false);
            return new TouringRecordListRecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TouringRecordListRecyclerViewHolder holder, int position) {
            holder.setEntity(touringRecordEntityList.get(position));
        }

        @Override
        public int getItemCount() {
            return touringRecordEntityList.size();
        }

        private class TouringRecordListRecyclerViewHolder extends RecyclerView.ViewHolder {
            private TextView dateText;
            private TextView placeText;
            private TouringRecordEntity entity;

            public TouringRecordListRecyclerViewHolder(@NonNull View itemView) {
                super(itemView);

                dateText = itemView.findViewById(R.id.rtrlcDateTextView);
                placeText = itemView.findViewById(R.id.rtrlcPlaceTextView);

                itemView.setOnClickListener(view -> {
                    Intent it = new Intent(TouringRecordListActivity.this, TouringRecordEditActivity.class);
                    it.putExtra(TouringRecordEditActivity.ENTITY_ID_KEY, entity.getTouringId());
                    startActivity(it);
                });
            }

            public void setEntity(TouringRecordEntity entity) {
                try {
                    this.entity = entity;
                    dateText.setText(Constant.DISPLAY_DATE_FORMAT.format(Constant.DB_DATE_FORMAT.parse(entity.getTouringDate())));
                    placeText.setText(entity.getTouringPlace());
                } catch (ParseException e) {
                    Log.e(Constant.LOG_TAG, e.getLocalizedMessage());
                }
            }
        }
    }
}