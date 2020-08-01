package com.example.autofill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.RequiresApi;

import android.os.Build;
import android.util.Log;

import java.util.Objects;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.autofill.MainActivity.sharedPrefFile;

public class AlarmReceiver extends BroadcastReceiver
        implements Response {
    public String mData = "";
    public String mSchoolNumber = "";
    public Context mcontext;
    private static final String TAG = "AlarmReceiver成功";

        @Override
    public void onReceive(Context context, Intent intent) {
            this.mcontext = context;
          SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile,
                MODE_PRIVATE);
        mData = sharedPreferences.getString("JSONSeniorString", "");
        Log.d(TAG, "成功恢复数据"+mData);
        mSchoolNumber = sharedPreferences.getString("SchoolnumberString", "");


        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && mData.length() != 0) {
            //执行自动打卡
            PositionParse getPosition = new PositionParse();
            getPosition.execute();

            FillStationParse fillStationParse = new FillStationParse();
            fillStationParse.delegate1 = this;
            fillStationParse.execute(mSchoolNumber);
            //这里需要判断checkposteddata做完了没有

            //网络状况判断
            } else if (mData.length() == 0) {
            //数据库错误
            Notify notify3 = new Notify(mcontext,"今日打卡失败","未能得到有效数据，服务器可能出现问题");
            notify3.deliverNotification();
            } else {
            //未知原因错误
            Notify notify2 = new Notify(mcontext,"今日打卡失败","网络连接不良，请重试");
            notify2.deliverNotification();
            }
        }

    @Override
    public void onPostFinish(String responseCode) {
        Log.d(TAG, responseCode);
        switch (responseCode){
            case "REFUSE":
                //服务器拒绝
                Notify notify4 = new Notify(mcontext,"今日打卡失败","服务器拒绝了打卡请求");
                notify4.deliverNotification();
                break;
            case "TODAYNOFILL":
                //今天还没打卡
                PostDataParse postDataParse = new PostDataParse();
                postDataParse.delegate = this;
                postDataParse.execute(mData);
                break;
            case "TODAYHASFILL":
                Notify notify3 = new Notify(mcontext,"今日已经打过卡","本APP不再重复打卡");
                notify3.deliverNotification();
                break;
            case  "OK":
                //打卡成功
                Notify notify1 = new Notify(mcontext,"今日打卡成功！","无需点击此通知");
                notify1.deliverNotification();
                break;
            default:
                //未知原因错误
                Notify notify2 = new Notify(mcontext,"今日打卡失败","未知原因错误，请重试");
                notify2.deliverNotification();
                break;
        }
    }
}
