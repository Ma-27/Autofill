package com.example.autofill.network;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpGet {

    private String locationUri;
    private String key;
    private String sig;


    public HttpGet(String locationUrl,String key,String sig){
        this.locationUri = locationUrl;
        this.key = key;
        this.sig = sig;
    }

    //position的uri
    protected String initialUri(){
        Uri builtURI = Uri.parse(locationUri).buildUpon()
                .appendQueryParameter("key", key)
                .appendQueryParameter("sig", sig)
                .build();
        return builtURI.toString();
    }

    protected String getData(){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String dataJSONString = "";
        try {
            URL requestURL = new URL(initialUri());
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
            dataJSONString = builder.toString();
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
        return dataJSONString;
    }

}

