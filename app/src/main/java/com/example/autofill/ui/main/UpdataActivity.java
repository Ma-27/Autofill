package com.example.autofill.ui.main;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationViewModel;

public class UpdataActivity extends AppCompatActivity{

    InformationViewModel informationViewModel;
    int id = 0;
    private String unparsedStation;

    private static final String TAG = "UpdataActivity成功";
    public static final String EXTRA_REPLY = "com.example.autofill.ui.main.REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle extras = getIntent().getExtras();
        String data = "";
        if(extras!=null){
            data = extras.getString("changedData","");
            id = extras.getInt("id",0);
            unparsedStation = extras.getString("unparsedStation","");
        }


        //Log.e(TAG, "活动加载on create一次，是否乱跳？");

        //Log.d(TAG, "onCreate:"+data);
        //不要用equal！！！否则崩掉
        if (data == "") {
            Toast.makeText
                    (UpdataActivity.this, "你填写的数据是空的！", Toast.LENGTH_SHORT).show();
        } else if (id == 0) {
            Toast.makeText
                    (UpdataActivity.this, "好像没有更新数据", Toast.LENGTH_SHORT).show();
        } else {
            informationViewModel = new InformationViewModel(getApplication());
            informationViewModel.updateSingle(new InformationEntity(encodeStation(data), id));
        }

        finish();
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

        /**
         * //该循环验证字符串是否按想要的方式分裂
         for (int i = 0; i < splitted.length; i++) {
         Log.d(TAG, "分裂字符串"+splitted[i]+"循环节i："+i);
         }
         */
        return splitted[0];
    }


}