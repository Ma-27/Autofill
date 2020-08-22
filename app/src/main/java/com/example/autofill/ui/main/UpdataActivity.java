package com.example.autofill.ui.main;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.autofill.storage.InformationEntity;

public class UpdataActivity extends AppCompatActivity{

    MrdkViewModel mrdkViewModel;
    InformationEntity currentEntity;
    int id = 0;
    private String unparsedStation;

    private static final String TAG = "UpdataActivity成功";
    public static final String EXTRA_REPLY = "com.example.autofill.ui.main.REPLY";


    public UpdataActivity(InformationEntity entity) {
        this.currentEntity = entity;
        this.unparsedStation = currentEntity.getStation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "活动加载on create一次，是否乱跳？");
        Intent intent  =getIntent();
        String s = "";

        Log.d(TAG, "onCreate:"+s);
        s = intent.getStringExtra("changedData");
        String data = s.toString();
        int id = currentEntity.getId();
        //不要用equal！！！否则崩掉
        if (data == "") {
            Toast.makeText
                    (UpdataActivity.this, "你填写的数据是空的！", Toast.LENGTH_SHORT).show();
        } else if (id == 0) {
            Toast.makeText
                    (UpdataActivity.this, "好像没有更新数据", Toast.LENGTH_SHORT).show();
        } else {
            //sendDataIntent.putExtra(EXTRA_REPLY,data);
            mrdkViewModel = new MrdkViewModel(getApplication());
            Log.e(TAG, "测试每次乱跳后的id: " + id);
            mrdkViewModel.updateSingle(new InformationEntity(encodeStation(data), id));
        }



    }

    private String encodeStation(String pendingStation) {
        StringBuilder builder = new StringBuilder();
        builder.append(parseJsonName(unparsedStation));
        builder.append("-");
        builder.append(pendingStation);
        return builder.toString();
    }

    private String parseJsonName(String unparsedStation) {
        String[] splitted = unparsedStation.split("-");

        /**该循环验证字符串是否按想要的方式分裂
         for (int i = 0; i < splitted.length; i++) {
         Log.d(TAG, "分裂字符串"+splitted[i]+"循环节i："+i);
         }
         */
        return splitted[0];
    }


}