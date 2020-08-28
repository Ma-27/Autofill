package com.example.autofill.network.connect;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpPost {

    private String uri;
    private String jsonParam;
    private static final String TAG = "HttpPost成功";

    public HttpPost(String uri,String jsonParam){
        this.uri = uri;
        this.jsonParam = jsonParam;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String PostData(){
        String responseMessage = null;
        try {
            URL url = new URL(uri);
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


            //将json utf8编码为字符串
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam);
            os.flush();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte[] buffer = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                is.close();
                message.close();
                responseMessage = new String(message.toByteArray());
                Log.d("STATUS", "查询次数成功状态码" + responseMessage);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "PostData: 看看数据是否有问题？"+responseMessage);
        return responseMessage;
    }

}
