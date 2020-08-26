package com.example.autofill.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.room.Room;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRoomDatabase;

public class AlarmReceiver extends BroadcastReceiver implements Response{

    private static final String TAG = "AlarmReceiver成功";
    private static InformationRoomDatabase INSTANCE;
    private InformationEntity[] information;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 打开了on receive");

        //获取数据
        if(INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context,
                    InformationRoomDatabase.class,
                    "fetch_post_database")
                    .build();
        }

        //获取数据
        FetchDataAsyncTask fetchDataAsyncTask = new FetchDataAsyncTask(INSTANCE);
        fetchDataAsyncTask.delegate0 = this;
        fetchDataAsyncTask.execute();
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();

        }

        //if (networkInfo != null && networkInfo.isConnected() && mData.length() != 0) {

        //}

    }

    @Override
    public void onPostFinish(String responseCode) {

    }

    //这个是恢复数据后做的
    @Override
    public void onPostFinish(InformationEntity[] informationEntities) {
        Log.d(TAG, "onPostFinish:执行到循环前");
        for(int i = 0;i<informationEntities.length;i++){
            Log.d(TAG, "onPostFinish: "+informationEntities[i]);
        }
    }
}
