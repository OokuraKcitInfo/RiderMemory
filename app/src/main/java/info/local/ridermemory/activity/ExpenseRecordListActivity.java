package info.local.ridermemory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.local.ridermemory.R;
import info.local.ridermemory.database.ApplicationViewModel;
import info.local.ridermemory.database.CategoryEntity;
import info.local.ridermemory.database.ExpenseRecordWithCategoryEntity;
import info.local.ridermemory.util.CategorySpinnerAdapter;
import info.local.ridermemory.util.Constant;

public class ExpenseRecordListActivity extends AppCompatActivity {
    private Spinner itemSpinner;
    private RecyclerView expenseDetailListRecyclerView;
    private TextView amountText;
    private ApplicationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_record_list);

        viewModel = new ViewModelProvider(this).get(ApplicationViewModel.class);
        initializedComponent();
        createItemSpinnerList();
        setExpenseRecordList();
        bindComponentEvents();
    }
    private void initializedComponent() {
        itemSpinner = findViewById(R.id.erlItemSpinner);
        expenseDetailListRecyclerView = findViewById(R.id.erlExpenseDetailList);
        amountText = findViewById(R.id.erlTotalMoneyTextView);
    }

    private void createItemSpinnerList() {
        viewModel
                .getCategoryListAsc()
                .observe(this, categoryList -> {
                    CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(
                            this
                            , android.R.layout.simple_spinner_item
                            , categoryList
                    );
                    adapter.insert(Constant.ALL_CATEGORY, 0);

                    itemSpinner.setAdapter(adapter);
                });
    }

    private void bindComponentEvents() {
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryEntity selectedEntity = (CategoryEntity)itemSpinner.getSelectedItem();
                viewModel.getCategoryIdCondition().postValue(selectedEntity.getCategoryId());
                viewModel.getExpenseRecordTotalling().observe(ExpenseRecordListActivity.this, expenseRecordTotallingEntity -> amountText.setText(expenseRecordTotallingEntity.getTotalAmount() + ""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.erlBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.erlAddButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, ExpenseRecordEditActivity.class);
            startActivity(intent);
        });
    }

    private void setExpenseRecordList() {
        viewModel.getExpenseRecordListDesc().observe(this, expenseRecordWithCategoryEntities -> {
            expenseDetailListRecyclerView.removeAllViews();
            ExpenseRecordListViewAdapter adapter = new ExpenseRecordListViewAdapter(expenseRecordWithCategoryEntities);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            expenseDetailListRecyclerView.setHasFixedSize(true);
            expenseDetailListRecyclerView.setLayoutManager(llm);
            expenseDetailListRecyclerView.setAdapter(adapter);
        });
    }

    private class ExpenseRecordListViewAdapter extends RecyclerView.Adapter<ExpenseRecordListViewAdapter.ExpenseRecordListViewHolder> {
        private final List<ExpenseRecordWithCategoryEntity> list;
        public ExpenseRecordListViewAdapter(List<ExpenseRecordWithCategoryEntity> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ExpenseRecordListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expense_record_list_cell, parent, false);
            return new ExpenseRecordListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseRecordListViewHolder holder, int position) {
            holder.setExpenseRecordEntity(list.get(position));
        }

        @Override
        public int getItemCount() { return list.size(); }

        private class ExpenseRecordListViewHolder extends RecyclerView.ViewHolder {
            ExpenseRecordWithCategoryEntity entity;
            TextView dateText;
            TextView categoryText;
            TextView expenseAmountText;

            public ExpenseRecordListViewHolder(@NonNull View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.rerlcDateTextView);
                categoryText = itemView.findViewById(R.id.rerlcItemTextView);
                expenseAmountText = itemView.findViewById(R.id.rerlcTotalMoneyTextView);

                itemView.setOnClickListener(v -> {
                    Intent it = new Intent(ExpenseRecordListActivity.this, ExpenseRecordEditActivity.class);
                    it.putExtra(ExpenseRecordEditActivity.ENTITY_KEY, entity.expenseRecordEntity);
                    startActivity(it);
                });
            }

            public void setExpenseRecordEntity(ExpenseRecordWithCategoryEntity entity) {
                this.entity = entity;
                setTextFromEntity();
            }

            public void setTextFromEntity() {
                dateText.setText(entity.expenseRecordEntity.getRecordDate());
                categoryText.setText(entity.categoryEntity.getCategoryName());
                expenseAmountText.setText(entity.expenseRecordEntity.getExpenseAmount() + "");
            }
        }
    }
}