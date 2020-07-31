package com.example.autofill;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;

public class CheckPostedNetworkUtils {

    private static int gettime() {
        long timeStamp = System.currentTimeMillis();
        return (int) (timeStamp / 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static String post(String schoolNumberString) {
        String responseMessage = null;
        try {
            CertificateFactory cf = null;
            try {
                URL url = new URL("https://we.cqu.pt/api/mrdk/get_mrdk_flag.php");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("xh",schoolNumberString);
                jsonParam.put("sign", "c4cb4739a0b923820scc509a6f75849b");
                jsonParam.put("timestamp", gettime());

                String s0 = jsonParam.toString();
                String s1 = new String(s0.getBytes(), StandardCharsets.UTF_8);
                //base64 编码一下
                byte[] data_encode = s1.getBytes();
                String stringbase64 = Base64.encodeToString(data_encode, Base64.DEFAULT);
                //构建查询数据
                JSONObject jsonParam1 = new JSONObject();
                jsonParam1.put("key", stringbase64);
                //将json utf8编码为字符串
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam1.toString());
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return responseMessage;
    }

}
