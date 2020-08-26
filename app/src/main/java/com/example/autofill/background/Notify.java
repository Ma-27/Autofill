package com.example.autofill.background;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.autofill.MainActivity;
import com.example.autofill.R;

public class Notify {
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private Context context;
    private String title;
    private String text;

    public Notify (Context contextin, String titlein, String text){
        this.context = contextin;
        this.title = titlein;
        this.text = text;
    }

    protected void deliverNotification() {
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //该FLAG_UPDATE_CURRENT标志告诉系统使用旧的Intent但替换extras数据。
        // 因为没有任何额外的功能，所以Intent可以调用PendingIntent反复使用相同的功能。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_done_autofill)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
