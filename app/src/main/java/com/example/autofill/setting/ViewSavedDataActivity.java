package com.example.autofill.setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.example.autofill.R;
import com.example.autofill.storage.InformationEntity;

import java.util.List;

public class ViewSavedDataActivity extends AppCompatActivity {

    private InformationViewModel informationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_data);
        Intent intent = getIntent();


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final InformationListAdapter adapter = new InformationListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    }
}