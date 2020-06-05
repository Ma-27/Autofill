package com.example.autofill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.autofill.MainActivity.sharedPrefFile;

public class GetPosition extends AsyncTask<String, Void, String> {

    private JSONObject jsonPositionParam = new JSONObject();
    public static String position = "";

    @Override
    protected String doInBackground(String... strings) {
        return GETNetWorkUtils.getLocation();
    }

    @Override
    protected void onPostExecute(String s1) {
        super.onPostExecute(s1);
        try {
            //转化为json object 层层解析
            JSONObject jsonObjectPure = new JSONObject(s1);
            String resultArray = jsonObjectPure.getString("result");
            JSONObject jsonObjectResult = new JSONObject(resultArray);
            String locationArray = jsonObjectResult.getString("location");
            int i = 0;
            String lat = null;
            String lng = null;
            while (i < resultArray.length()) {
                try {
                    JSONObject jsonObjectLocation = new JSONObject(locationArray);
                    lat = jsonObjectLocation.getString("lat");
                    lng = jsonObjectLocation.getString("lng");
                } catch (Exception e) {
                    position = "在解析位置时出错";
                    e.printStackTrace();
                }
                i++;
            }
            // If both are found, display the result.
            if (lat != null && lng != null) {
                try{
                    jsonPositionParam.put("lat",lat);//latitude
                    jsonPositionParam.put("lng",lng);//longitude
                    position = jsonPositionParam.toString();
                    Log.d(TAG, "成功解析到了位置"+position);
                } catch (Exception e) {
                    position = "在构建位置参数时出错";
                    e.printStackTrace();
                }

            } else {
                position = "未成功获取经纬度，请重试";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

