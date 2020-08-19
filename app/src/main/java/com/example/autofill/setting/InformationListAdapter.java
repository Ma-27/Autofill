package com.example.autofill.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofill.R;
import com.example.autofill.storage.InformationEntity;

import java.util.ArrayList;
import java.util.List;

public class InformationListAdapter extends RecyclerView.Adapter<InformationListAdapter.DataViewHolder> {

    private List<InformationEntity> information;
    private Context context;
    private static final String TAG = "InformationListAdapter成功";
    private ArrayList<DataCacheHolder> dataCacheHolder;


    //该类的构造方法
    public InformationListAdapter(Context context,ArrayList<DataCacheHolder> dataCacheHolder) {
        this.context = context;
        this.dataCacheHolder = dataCacheHolder;
    }

    //holder 内部类
    class DataViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        private TextView titleItemView;
        private TextView contentItemView;

        @SuppressLint("CutPasteId")
        private DataViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            titleItemView = itemView.findViewById(R.id.item_title);
            contentItemView = itemView.findViewById(R.id.item_title);
        }
    }


    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.setting_recyclerview_item,parent,false);
       return new DataViewHolder(itemView);
    }



    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        InformationEntity currentEntity = information.get(position);
        DataCacheHolder cacheHolder = dataCacheHolder.get(position);

        holder.contentItemView.setText(parseStation(currentEntity.getStation()));
       // Log.d(TAG, "恢复 "+currentEntity.getStation());
        //holder.contentItemView.setText(cacheHolder.getData());
        holder.titleItemView.setText(cacheHolder.getTitle()+":");
    }

    @Override
    public int getItemCount() {

        if (information != null)
            return information.size();
        else return 0;
    }

    void setData(List<InformationEntity> data){
        information = data;
        notifyDataSetChanged();
    }

    @SuppressLint("LongLogTag")
    String parseStation(String unparsedStation){
        String[] splitted = unparsedStation.split("-");
        for (int i = 0; i < splitted.length; i++) {
            Log.d(TAG, "分裂字符串"+splitted[i]+"循环节i："+i);
        }
        return splitted[1];
    }
}
