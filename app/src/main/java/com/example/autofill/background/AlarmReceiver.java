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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver implements Response{

    private static final String TAG = "AlarmReceiver成功";
    private static InformationRoomDatabase INSTANCE;
    private InformationEntity[] information;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 打开了on receive");

        //获取数据
        if(INSTANCE==null) {
            INSTANCE = InformationRoomDatabase.getDatabase(context);
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
    public void onPostFinish(List<InformationEntity> informationEntities) throws JSONException {
        JSONObject jsonJuniorParam = new JSONObject();
        for(int i = 0;i<informationEntities.size();i++){
            InformationEntity current = informationEntities.get(i);
            //开始转换json字符串
            if(parseName(current.getStation())!="图片占位"
                    ||parseName(current.getStation())!="有无咳嗽、乏力、鼻塞、流涕、咽痛、腹泻等症状"){
                jsonJuniorParam.put(
                        parseName(current.getStation()),
                        parseStation(current.getStation())
                );
            }
        }
        Log.d(TAG, "onPostFinish: 最终结果"+  jsonJuniorParam.toString()  );
    }


    /**
     * 这个用来分裂字符串，将room database里的数据真正转换成你填写的数据
     * @param unparsedStation 未分解的数据
     * @return 分解完的数据
     */
    String parseStation(String unparsedStation) {
        String[] splitted = unparsedStation.split("-");
        return splitted[1];
    }

    /**
     * 这个用来分裂字符串，获取json object的名字
     * @param unparsedStation
     * @return
     */
    String parseName(String unparsedStation) {
        String[] splitted = unparsedStation.split("-");
        return splitted[0];
    }
}
