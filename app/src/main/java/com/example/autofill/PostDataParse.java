package com.example.autofill;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.autofill.network.HttpPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static com.example.autofill.PositionParse.position;

public class PostDataParse extends AsyncTask<String, Void, String> {

    private static final String TAG = "sfs";
    Response delegate = null;
    private String returnmessage = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        String jsonFinalString = "";
        JSONObject jsonParam = new JSONObject();
        String s0 = "";
        s0 = buildFinalJSONString(strings[0]);
        Log.d(TAG, "成功最终提交内容" + s0);
        String s1 = new String(s0.getBytes(), StandardCharsets.UTF_8);
        //base64 编码一下
        byte[] data_encode = s1.getBytes();
        String stringbase64 = Base64.encodeToString(data_encode, Base64.DEFAULT);
        //构建终极打卡数据
        JSONObject jsonParam1 = new JSONObject();
        try {
            jsonParam1.put("key", stringbase64);
            jsonFinalString = jsonParam1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpPost httpPostData = new
                HttpPost("https://we.cqu.pt/api/mrdk/post_mrdk_info.php",jsonFinalString);
        Log.d(TAG, "doInBackground: "+jsonFinalString);
        return httpPostData.PostData();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "成功出错点"+s);
        if(s!=null){
            try {
                //转化为json object 层层解析
                JSONObject jsonObject = new JSONObject(s);
                int status = 0;
                status = jsonObject.getInt("status");
                if(status == 200){
                    returnmessage = "OK";
                    Log.d(TAG, "成功出错点"+returnmessage);
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
        delegate.onPostFinish(returnmessage);
    }

    private String buildFinalJSONString(String data){
        String finalString  ="";
        String jsonSeniorStringLocal = "";
        jsonSeniorStringLocal = data;

        try {
            JSONObject jsonFinalObject1 = new JSONObject(jsonSeniorStringLocal);
            JSONObject jsonFinalObject2 = new JSONObject(position);
            JSONObject mergedObj = new JSONObject();
            mergedObj.put("timestamp",gettime());
            Iterator i1 = jsonFinalObject1.keys();
            Iterator i2 = jsonFinalObject2.keys();
            String tmp_key;
            while(i1.hasNext()) {
                tmp_key = (String) i1.next();
                mergedObj.put(tmp_key, jsonFinalObject1.get(tmp_key));
            }
            while(i2.hasNext()) {
                tmp_key = (String) i2.next();
                mergedObj.put(tmp_key, jsonFinalObject2.get(tmp_key));
            }
            finalString = mergedObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "成功finalstringpos"+position);
        return finalString;
    }

    private int gettime() {
        long timeStamp = System.currentTimeMillis();
        return (int) (timeStamp / 1000);
    }
}
