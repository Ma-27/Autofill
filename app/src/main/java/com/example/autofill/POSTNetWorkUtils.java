package com.example.autofill;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import static com.example.autofill.GetPosition.position;

public class POSTNetWorkUtils {
    private static final String TAG = "sfs";
    public SharedPreferences mPreferences;
    private static int responseCode = 000;

    private static int gettime() {
        long timeStamp = System.currentTimeMillis();
        int timeStamp1 = (int) (timeStamp / 1000);
        Log.d("1", "成功" + timeStamp1);
        return timeStamp1;
    }

    private static String buildFinalJSONString(String data){
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
        Log.d(TAG, "成功finalstring"+position);
        return finalString;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static String post(String Data) {
        String responseMessage = null;
        try {
            try {
                URL url = new URL("https://we.cqu.pt/api/mrdk/post_mrdk_info.php");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                // 创建ssl
                SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setRequestMethod("POST");
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                conn.setRequestProperty("Host", "we.cqu.pt");
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143"
                        + "Safari/537.36 MicroMessenger/7.0.4.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
                conn.setRequestProperty("Referer", "https://servicewechat.com/wx8227f55dc4490f45/25/page-frame.html");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                //将初始输入的json 字符串转化为utf8 编码的字符串
                String s0 = "";
                s0 = buildFinalJSONString(Data);
                Log.d(TAG, "成功最终提交内容" + s0);
                String s1 = new String(s0.getBytes(), StandardCharsets.UTF_8);
                //base64 编码一下
                byte[] data_encode = s1.getBytes();
                String stringbase64 = Base64.encodeToString(data_encode, Base64.DEFAULT);

                //构建终极打卡数据
                JSONObject jsonParam1 = new JSONObject();
                jsonParam1.put("key", stringbase64);

                //将json utf8编码为字符串
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam1.toString());
                os.flush();
                responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 获取响应的输入流对象
                    InputStream is = conn.getInputStream();
                    // 创建字节输出流对象
                    ByteArrayOutputStream message = new ByteArrayOutputStream();
                    // 定义读取的长度
                    int len = 0;
                    // 定义缓冲区
                    byte buffer[] = new byte[1024];
                    // 按照缓冲区的大小，循环读取
                    while ((len = is.read(buffer)) != -1) {
                        // 根据读取的长度写入到os对象中
                        message.write(buffer, 0, len);
                    }
                    // 释放资源
                    is.close();
                    message.close();
                    String msg = new String(message.toByteArray());
                    responseMessage = msg;
                }
                Log.d("MSG", "成功返回消息" + responseMessage);
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return responseMessage;
    }
}
