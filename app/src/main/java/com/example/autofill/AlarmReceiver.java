package com.example.autofill;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.autofill.MainActivity.sharedPrefFile;

public class AlarmReceiver extends BroadcastReceiver
        implements ResponseCode1Interface,ResponseCodeInterface {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    public String mData = "";
    public String mSchoolNumber = "";
    public Context mcontext;

        @Override
    public void onReceive(Context context, Intent intent) {
            this.mcontext = context;
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Log.d(TAG, "执行一次通知推送成功");

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefFile,
                MODE_PRIVATE);
        mData = sharedPreferences.getString("JSONSeniorString", "");
        //Log.d(TAG, "成功恢复数据"+mData);
        mSchoolNumber = sharedPreferences.getString("SchoolnumberString", "");


        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && mData.length() != 0) {
            //执行自动打卡
            GetPosition getPosition = new GetPosition();
            getPosition.execute();

            CheckPostedData checkPostedData = new CheckPostedData();
            checkPostedData.delegate1 = this;
            checkPostedData.execute(mSchoolNumber);
            //这里需要判断checkposteddata做完了没有

            //网络状况判断
            } else if (mData.length() == 0) {
            //数据库错误
            deliverNotification2(context);
            } else {//未知错误
                deliverNotification2(context);
            }
        }


    //打卡成功
    private void deliverNotification1(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //该FLAG_UPDATE_CURRENT标志告诉系统使用旧的Intent但替换extras数据。
        // 因为没有任何额外的功能，所以Intent可以PendingIntent反复使用相同的功能。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_done_autofill)
                .setContentTitle("今日已成功打卡！")
                .setContentText("不需要点击这条通知。如果今天已经手动打卡成功，此消息可以忽略")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    //未知错误
    private void deliverNotification2 (Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //该FLAG_UPDATE_CURRENT标志告诉系统使用旧的Intent但替换extras数据。
        // 因为没有任何额外的功能，所以Intent可以PendingIntent反复使用相同的功能。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_done_autofill)
                .setContentTitle("今日打卡失败")
                .setContentText(" 未知原因错误，请手动打卡")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    //今日已打过卡
    private void deliverNotification3 (Context context){
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //该FLAG_UPDATE_CURRENT标志告诉系统使用旧的Intent但替换extras数据。
        // 因为没有任何额外的功能，所以Intent可以PendingIntent反复使用相同的功能。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_done_autofill)
                .setContentTitle("今日已打过卡")
                .setContentText("本APP不再重复打卡")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    //服务器拒绝了打卡请求
    private void deliverNotification4 (Context context){
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //该FLAG_UPDATE_CURRENT标志告诉系统使用旧的Intent但替换extras数据。
        // 因为没有任何额外的功能，所以Intent可以PendingIntent反复使用相同的功能。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_done_autofill)
                .setContentTitle("今日打卡失败")
                .setContentText("打卡请求被服务器拒绝")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onCheckPostedFinish(String responsecode1) {
        Log.d(TAG, "成功returnmessage1"+responsecode1);
        if (responsecode1.equals("TODAYNOFILL")) {
            PostData postData = new PostData();
            postData.delegate = this;
            postData.execute(mData);
        } else if (responsecode1.equals("TODAYHASFILL")) {
            deliverNotification3(mcontext);
            Log.d(TAG, "成功执行");
        }else{
            deliverNotification2(mcontext);
        }
    }


    @Override
    public void onPostDataFinish(String responsecode) {
        if (responsecode == "OK") {
            deliverNotification1(mcontext);
        } else if (responsecode == "REFUSE") {
            deliverNotification4(mcontext);
        } else {
            deliverNotification2(mcontext);
        }
    }
}
