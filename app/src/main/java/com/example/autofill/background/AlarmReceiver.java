package com.example.autofill.background;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.autofill.R;
import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationViewModel;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private InformationViewModel informationViewModel;
    private static final String TAG = "AlarmReceiver成功";
    private List<InformationEntity> information;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        //informationViewModel = new InformationViewModel((Application) context);

        Log.d(TAG, "onReceive:成功获取information "+information);

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        //if (networkInfo != null && networkInfo.isConnected() && mData.length() != 0) {

        //}

    }
}
