package com.example.autofill.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofill.R;
import com.example.autofill.setting.InformationListAdapter;
import com.example.autofill.storage.InformationEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 复制自informationListAdapter并作改编
 */
public class MrdkListAdapter extends RecyclerView.Adapter<MrdkListAdapter.MrdkViewHolder> {

    private List<InformationEntity> information;
    private Context context;
    private static final String TAG = "MrdkListAdapter成功";
    private ArrayList<MrdkCacheHolder> dataCacheHolder;

    public MrdkListAdapter(Context context,ArrayList<MrdkCacheHolder> mrdkCacheHolder){
        this.context = context;
        this.dataCacheHolder = mrdkCacheHolder;
    }


    /**
     * 用来暂存的内部类
     */
    class MrdkViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        private final TextView titleItemView;

        public MrdkViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "MrdkViewHolder: ");
            this.itemView = itemView;
            this.titleItemView = itemView.findViewById(R.id.mrdkTextView);
        }
    }


    @NonNull
    @Override
    public MrdkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.mrdk_recycler_item,parent,false);
        return new MrdkListAdapter.MrdkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MrdkViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        InformationEntity currentEntity = information.get(position);
        Log.d(TAG, "onBindViewHolder: 数据"+currentEntity.getStation());
        holder.titleItemView.setText(parseStation(currentEntity.getStation()));
    }

    @Override
    public int getItemCount() {
        if(information!=null){
            return information.size();
        }else
            return 0;
    }

    String parseStation(String unparsedStation){
        String[] splitted = unparsedStation.split("-");

        for (int i = 0; i < splitted.length; i++) {
            Log.d(TAG, "分裂字符串"+splitted[i]+"循环节i："+i);
        }

        return splitted[1];
    }

    void setData(List<InformationEntity> data){
        information = data;
        notifyDataSetChanged();
    }
}
