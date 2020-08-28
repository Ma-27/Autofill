package com.example.autofill.background.dirtyRestart;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import com.example.autofill.R;
import com.example.autofill.background.AlarmReceiver;
import com.example.autofill.background.dirtyRestart.Restarter;
import com.example.autofill.storage.InformationViewModel;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

public class TimingServiceDitry extends Service {

    private AlarmManager alarmManager;
    private PendingIntent checkAndPostPendingIntent;
    private static final int REQUEST_CODE = 0;
    private static final String TAG = "TimingService成功";


    public TimingServiceDitry() { }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //intent 初始化
        Intent checkAndPostIntent = new Intent(this, AlarmReceiver.class);

        //pending intent初始化
        checkAndPostPendingIntent = PendingIntent.getBroadcast
                (this,REQUEST_CODE,checkAndPostIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //alarm 设置
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //间隔启动，一分钟或者半天
        long repeatInterval = 1;
                //AlarmManager.INTERVAL_HALF_DAY;

        long triggerTime = SystemClock.elapsedRealtime()+repeatInterval;

        if (alarmManager != null) {
            alarmManager.setInexactRepeating
                    (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, repeatInterval,
                            checkAndPostPendingIntent
                    );
        }
        Log.d(TAG, "onStartCommand: 成功启动");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 这是在后台想要杀掉这个service时强制重启（频繁自启动）
     * 这不是我想要的样子
     * 但是迫于国内生态环境，应用不能长留后台，也只能出此下策保证app不会被杀
     * 那只能以不干净的吃相和性能的牺牲来实现目标了
     * 未来会更新firebase fcm推送，方便有google框架的同学不使用这种方式
     * 然而这种屠龙少年成为恶龙的故事，总不免令人感到失望。
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: ");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);

        //干完了再移除
        super.onTaskRemoved(rootIntent);
    }


}
