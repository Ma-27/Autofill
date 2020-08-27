package com.example.autofill.background;

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
import com.example.autofill.storage.InformationViewModel;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

public class TimingService extends Service {

    private AlarmManager alarmManager;
    private PendingIntent checkAndPostPendingIntent;
    private static final int REQUEST_CODE = 0;
    private static final String TAG = "TimingService成功";


    public TimingService() { }

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
        long repeatInterval = //1;
                AlarmManager.INTERVAL_HALF_DAY;

        long triggerTime = SystemClock.elapsedRealtime()+repeatInterval;

        if (alarmManager != null) {
            alarmManager.setInexactRepeating
                    (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, repeatInterval,
                            checkAndPostPendingIntent
                    );
        }
        Log.d(TAG, "onStartCommand: 成功启动后台");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alarmManager != null) {
            alarmManager.cancel(checkAndPostPendingIntent);
        }
    }

}
