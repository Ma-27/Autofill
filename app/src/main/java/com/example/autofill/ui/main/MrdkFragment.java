package com.example.autofill.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.autofill.R;
import com.example.autofill.storage.InformationEntity;

import java.util.ArrayList;
import java.util.List;

public class MrdkFragment extends Fragment {

    private MrdkViewModel mrdkViewModel;
    private MrdkListAdapter adapter;
    private ArrayList<MrdkCacheHolder> mrdkCacheHolder;
    private static final String TAG = "MrdkFragment成功";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mrdk_fragment, container, false);

        mrdkCacheHolder = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.mrdk_recyclerview);
        adapter = new MrdkListAdapter(getActivity(),mrdkCacheHolder);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Log.d(TAG, "onCreateView: ");
        initializeData();
        mrdkViewModel = ViewModelProviders.of(this).get(MrdkViewModel.class);
        // TODO: Use the ViewModel
        mrdkViewModel.getAllData().observe(getActivity(), new Observer<List<InformationEntity>>() {
            @Override
            public void onChanged(List<InformationEntity> informationEntities) {
                adapter.setData(informationEntities);
                //Log.d(TAG, "onChanged: ");
            }
        });
        initializeData();
        return view;
    }

    private void initializeData() {
        String[] titleList = getResources().getStringArray(R.array.data_junior_title);
        String[] contentList = getResources().getStringArray(R.array.data_junior_name);
        String[] dataList = getResources().getStringArray(R.array.data_junior);
        TypedArray imageArray = getResources().obtainTypedArray(R.array.layout_images);

        for(int i=0;i<titleList.length;i++){
            StringBuilder builder = new StringBuilder();
            builder.append(contentList[i]);
            builder.append("-");
            builder.append(dataList[i]);

            // Log.d(TAG, "initializeData: 得到builder"+builder.toString());
            mrdkCacheHolder.add(
                    new MrdkCacheHolder(
                            titleList[i],
                            contentList[i],
                            "",
                            imageArray.getResourceId(i,0)
                    ));
            ////builder.toString())
        }
        //创建数据后，清理图片资源
        imageArray.recycle();
        adapter.notifyDataSetChanged();
    }
}