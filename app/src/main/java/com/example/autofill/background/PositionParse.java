package com.example.autofill.background;

import android.os.AsyncTask;
import android.util.Log;

import com.example.autofill.network.HttpGet;

import org.json.JSONException;
import org.json.JSONObject;

public class PositionParse extends AsyncTask<String, Void, String> {

    private JSONObject jsonPositionParam = new JSONObject();
    public static String position = "";
    private static final String TAG = "PositionParse成功";

    @Override
    protected String doInBackground(String... strings) {
        //调用网络get模块去获取经纬度
        String result;
        final String LOCATION_BASE_URL =  "https://apis.map.qq.com/ws/location/v1/ip";
        final String KEY_PARAM = "IVOBZ-QNW6P-SUKDY-LFQSE-LUFCJ-3CFUE";
        final String SIG_PARAM = "afebe5ad5227ec75a1f3d8b97f888cda";
        HttpGet httpGet = new HttpGet(LOCATION_BASE_URL,KEY_PARAM,SIG_PARAM);
        result = httpGet.getData();
        return result;
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
                    //Log.d(TAG, "成功解析到了位置"+position);
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
