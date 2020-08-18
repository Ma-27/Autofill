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

import java.util.List;

public class InformationListAdapter extends RecyclerView.Adapter<InformationListAdapter.DataViewHolder> {

    private List<InformationEntity> information;
    private Context context;
    private static final String TAG = "InformationListAdapter成功";


    //该类的构造方法
    public InformationListAdapter(Context context) {
        this.context = context;
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

        Log.d(TAG, "onBindViewHolder: "+currentEntity.getStation());

        holder.contentItemView.setText(currentEntity.getStation());
        holder.titleItemView.setText("afaffafa");
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
}
