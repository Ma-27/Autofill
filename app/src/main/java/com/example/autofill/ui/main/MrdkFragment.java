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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.autofill.R;
import com.example.autofill.storage.InformationEntity;

import java.util.ArrayList;
import java.util.List;

public class MrdkFragment extends Fragment {

    MrdkViewModel mrdkViewModel;
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

        mrdkViewModel = ViewModelProviders.of(this).get(MrdkViewModel.class);
        // TODO: 使用ViewModel
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
        //显示在layout上的title
        String[] titleList = getResources().getStringArray(R.array.data_junior_title);
        //json 字符串 数据的名字
        String[] dataNameList = getResources().getStringArray(R.array.data_junior_name);
        //获取每个对象的可见性
        int[] visibility = getResources().getIntArray(R.array.visibliity);
        //获取图片
        TypedArray imageArray = getResources().obtainTypedArray(R.array.layout_images);
        //获取edit text的暗示
        String[] contentHintList = getResources().getStringArray(R.array.data_hint);

        String[] yesData = getResources().getStringArray(R.array.data_yes);

        String[] noData = getResources().getStringArray(R.array.data_no);
        for(int i=0;i<titleList.length;i++){
            /**
             * 创建数据，放入entity中
            StringBuilder builder = new StringBuilder();
            builder.append(contentList[i]);
            builder.append("-");
            builder.append(visibility[i]);
             builder.toString())
             */
            // Log.d(TAG, "initializeData: 得到builder"+builder.toString());
            mrdkCacheHolder.add(
                    new MrdkCacheHolder(
                            titleList[i],
                            dataNameList[i],
                            visibility[i],
                            imageArray.getResourceId(i,0),
                            contentHintList[i],
                            yesData[i],
                            noData[i]
                    ));
        }
        //创建数据后，清理图片资源
        imageArray.recycle();
        adapter.notifyDataSetChanged();
    }


        public void updateData(String data) {
            if (mrdkViewModel!= null) {
                mrdkViewModel.updateSingle(new InformationEntity(data, 1));
            }
        }


}