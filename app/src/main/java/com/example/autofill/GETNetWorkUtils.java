package com.example.autofill;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GETNetWorkUtils {

    private static final String LOG_TAG =
            GETNetWorkUtils.class.getSimpleName();

    private static final String BOOK_BASE_URL =  "https://apis.map.qq.com/ws/location/v1/ip";
    private static final String KEY_PARAM = "key";
    private static final String SIG_PARAM = "sig";


    static String getLocation(){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try {
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, "IVOBZ-QNW6P-SUKDY-LFQSE-LUFCJ-3CFUE")
                    .appendQueryParameter(SIG_PARAM, "afebe5ad5227ec75a1f3d8b97f888cda")
                    .build();

            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // 获取输入流.
            InputStream inputStream = urlConnection.getInputStream();
            // 创建缓冲区
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
              //添加空行，让debug很容易
                builder.append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            bookJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookJSONString;
    }

}

