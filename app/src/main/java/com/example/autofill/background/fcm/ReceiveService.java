package com.example.autofill.background.fcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ReceiveService extends FirebaseMessagingService {

    private static final String TAG = "ReceiveService成功";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: 生成了新的令牌："+s);
        Toast.makeText(getApplicationContext(),"生成了新的令牌："+s,Toast.LENGTH_SHORT).show();
    }
}
