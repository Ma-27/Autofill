package com.example.autofill;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

public class PostData extends AsyncTask<String, Void, String> {

    private static final String TAG = "sfs";
    ResponseCodeInterface delegate = null;
    private String returnmessage = "";
    private static final String LOG_TAG = "Postdata";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        String s = "";
        s = POSTNetWorkUtils.post(strings[0]);
        Log.d(TAG, "成功执行打卡返回信息"+s);
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s!=null){
            try {
                //转化为json object 层层解析
                JSONObject jsonObject = new JSONObject(s);
                int status = 0;
                status = jsonObject.getInt("status");
                if(status == 200){
                    returnmessage = "OK";
                }else if (status == 403){
                    returnmessage = "REFUSE";
                }else {
                    returnmessage = "UNKNOWNERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // 空指针错误
            returnmessage = "UNKNOWNERROR";
        }
        delegate.onPostDataFinish(returnmessage);
    }
}
