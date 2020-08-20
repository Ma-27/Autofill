package com.example.autofill.ui.main;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.autofill.R;
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

    private static final int PHOTO = 0;
    private static final int RADIO = 2;
    private static final int EDIT = 1;

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
        private ImageView mrdkImage;
        private EditText editInfo;
        private RadioGroup selectGroup;

        public MrdkViewHolder(@NonNull View itemView) {
            super(itemView);
            //Log.d(TAG, "MrdkViewHolder: ");
            this.itemView = itemView;
            this.titleItemView = itemView.findViewById(R.id.mrdk_title);
            this.mrdkImage = itemView.findViewById(R.id.mrdk_image);
            this.selectGroup = itemView.findViewById(R.id.select_radiogroup);
            this.editInfo = itemView.findViewById(R.id.edit_data);

        }
    }


    @NonNull
    @Override
    public MrdkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.mrdk_recycler_item,parent,false);
        return new MrdkListAdapter.MrdkViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MrdkViewHolder holder, int position) {
        InformationEntity currentEntity = information.get(position);
        MrdkCacheHolder cacheHolder = dataCacheHolder.get(position);
        Log.d(TAG, "onBindViewHolder:测试 "+cacheHolder.getVisibility());

        //设置可见性
        switch (cacheHolder.getVisibility()){
            case PHOTO:
                holder.mrdkImage.setVisibility(View.VISIBLE);
                holder.titleItemView.setVisibility(View.GONE);
                holder.editInfo.setVisibility(View.GONE);
                holder.selectGroup.setVisibility(View.GONE);
                break;

            case EDIT:
                holder.mrdkImage.setVisibility(View.GONE);
                holder.titleItemView.setVisibility(View.VISIBLE);
                holder.editInfo.setVisibility(View.VISIBLE);
                holder.selectGroup.setVisibility(View.GONE);
                break;

            case RADIO:
                holder.mrdkImage.setVisibility(View.GONE);
                holder.titleItemView.setVisibility(View.VISIBLE);
                holder.editInfo.setVisibility(View.GONE);
                holder.selectGroup.setVisibility(View.VISIBLE);
                break;

            default:
                holder.mrdkImage.setVisibility(View.VISIBLE);
                holder.titleItemView.setVisibility(View.VISIBLE);
                holder.editInfo.setVisibility(View.VISIBLE);
                holder.selectGroup.setVisibility(View.VISIBLE);
        }
        //Log.d(TAG, "onBindViewHolder: 数据"+currentEntity.getStation());

        holder.editInfo.setText(parseStation(currentEntity.getStation()));

        //加载标题
        if(holder.titleItemView.getVisibility()==View.VISIBLE){
            holder.titleItemView.setText(cacheHolder.getTitle()+":");
        }

        //加载图片
        if (holder.mrdkImage.getVisibility()==View.VISIBLE){
            Glide.with(context).load(cacheHolder.getImage()).into(holder.mrdkImage);
        }

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

        /**该循环验证字符串是否按想要的方式分裂
        for (int i = 0; i < splitted.length; i++) {
            Log.d(TAG, "分裂字符串"+splitted[i]+"循环节i："+i);
        }
         */
        return splitted[1];
    }

    void setData(List<InformationEntity> data){
        information = data;
        notifyDataSetChanged();
    }
}
