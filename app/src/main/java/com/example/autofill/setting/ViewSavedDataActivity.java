package com.example.autofill.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.autofill.R;
import com.example.autofill.storage.InformationEntity;
import com.example.autofill.ui.setting.DataCacheHolder;
import com.example.autofill.ui.setting.InformationListAdapter;
import com.example.autofill.ui.setting.InformationViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewSavedDataActivity extends AppCompatActivity {

    private InformationViewModel informationViewModel;
    InformationListAdapter adapter;
    private ArrayList<DataCacheHolder> dataCacheHolder;
    private static final String TAG = "ViewSavedDataActivity成功";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_data);
        Intent intent = getIntent();
        dataCacheHolder = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new InformationListAdapter(this,dataCacheHolder);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        /**
         用于ViewModelProviders将ViewModel与UI控制器关联。
         当应用首次启动时，ViewModelProviders该类会创建ViewModel。
         当活动被破坏（例如通过配置更改）时，ViewModel持久性仍然存在。
         重新创建活动时，ViewModelProviders返回现有的ViewModel。
         */

        informationViewModel = ViewModelProviders.of(this).get(InformationViewModel.class);
        informationViewModel.getAllData().observe(this, new Observer<List<InformationEntity>>() {
            @Override
            public void onChanged(List<InformationEntity> informationEntities) {
                adapter.setData(informationEntities);
            }
        });
        initializeData();
    }

    private void initializeData() {
        String[] titleList = getResources().getStringArray(R.array.data_junior_title);
        String[] contentList = getResources().getStringArray(R.array.data_junior_name);
        String[] dataList = getResources().getStringArray(R.array.data_junior);

        for(int i=0;i<titleList.length;i++){
            StringBuilder builder = new StringBuilder();
            builder.append(contentList[i]);
            builder.append("-");
            builder.append(dataList[i]);
           // Log.d(TAG, "initializeData: 得到builder"+builder.toString());
            dataCacheHolder.add(new DataCacheHolder(
                    titleList[i],
                    contentList[i],
                    ""//builder.toString()
            ));
            adapter.notifyDataSetChanged();
        }


    }
}