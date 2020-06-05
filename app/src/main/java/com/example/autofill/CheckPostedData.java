package com.example.autofill;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import static android.content.ContentValues.TAG;


public class CheckPostedData extends AsyncTask<String, Void, String> {
    public String returnmessage1 = "";
    private static final String LOG_TAG = "CheckPostedTimes";
    public ResponseCode1Interface delegate1 = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        return CheckPostedNetworkUtils.post(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if(s!=null) {
                JSONObject jsonObject = new JSONObject(s);
                String resultArray = jsonObject.getString("data");
                JSONObject jsonObjectData = new JSONObject(resultArray);
                int count = 100;
                count = jsonObjectData.getInt("count");
                Log.d(TAG, "成功count" + count);
                if (count != 100) {
                    try {
                        if (count == 0)
                            returnmessage1 = "TODAYNOFILL";
                        else {
                            returnmessage1 = "TODAYHASFILL";
                        }
                    } catch (Exception e) {
                        returnmessage1 = "UNKNOWN_ERROR";
                        e.printStackTrace();
                    }
                } else {
                    returnmessage1 = "NULL_ERROR";
                }
            }else {
                Log.d(TAG, "成功失败，检查打卡信息时返回的s为空");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        delegate1.onCheckPostedFinish(returnmessage1);

    }
}
