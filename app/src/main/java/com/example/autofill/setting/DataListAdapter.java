package com.example.autofill.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofill.R;
import com.example.autofill.storage.InformationTable;

import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataViewHolder> {

    private final LayoutInflater inflater;
    private List<InformationTable> information;
    private Context context;


    //该类的构造方法
    public DataListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    //holder 内部类
    class DataViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private final TextView titleItemView;
        private final TextView contentItemView;

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
        /*
        return new DataListAdapter.DataViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.setting_recyclerview_item, parent, false));

         */
        return super.createViewHolder(parent, viewType);
    }



    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        InformationTable current = information.get(position);
        holder.contentItemView.setText(current.getStation());
    }

    @Override
    public int getItemCount() {
        if (information != null)
            return information.size();
        else return 0;
    }

    void setData(List<InformationTable> data){
        information = data;
        notifyDataSetChanged();
    }
}
