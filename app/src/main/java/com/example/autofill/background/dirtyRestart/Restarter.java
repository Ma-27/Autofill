package com.example.autofill.background.dirtyRestart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.autofill.background.TimingService;

/**
 * 这不是我想要的样子
 * 但是迫于国内生态环境，应用不能长留后台，也只能出此下策保证app不会被杀
 * 那只能以不干净的吃相和性能的牺牲来实现目标了
 * 未来会更新firebase fcm推送，方便有google框架的同学不使用这种方式
 * 然而这种屠龙少年成为恶龙的故事，总不免令人感到失望。
 */
public class Restarter extends BroadcastReceiver {

    private static final String TAG = "Restarter成功";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 重新启动timing service");
        Intent serviceIntent = new Intent(context, TimingService.class);
        context.startService(serviceIntent);
    }
}
