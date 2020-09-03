package com.example.autofill;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.autofill.network.prepareRequest.FillStationParse;
import com.example.autofill.background.dirtyRestart.TimingServiceDitry;
import com.example.autofill.setting.SettingsActivity;
import com.example.autofill.ui.main.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
import static com.example.autofill.ui.main.MrdkListAdapter.xh;

public class MainActivity extends AppCompatActivity{

    private SharedPreferences preferences;
    Boolean isOn = false;
    AlertDialog.Builder viewMyAlertBuilder;
    AlertDialog.Builder viewOthersAlertBuilder;

    //通知
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    /**常数判断是否关掉通知功能
     * 用@param Removetask，1的时候就是开启状态常驻后台；0的时候是关闭状态。取消常驻后台
     */
    public static int Removetask = 0;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private static final String TAG = "MainActivity成功";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tab界面设置
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /*
        //开启后台推送
        if(!isIgnoringBatteryOptimizations());{
            requestIgnoreBatteryOptimizations();
        }

         */

        //获取shared preferences
        preferences = getSharedPreferences("switchStation",MODE_PRIVATE);
        isOn = preferences.getBoolean("isOn", false);

        //设置通知channel
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        //初始化notification channel
        createNotificationChannel();
    }

    /**
     * 显示三个点
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 选中三个点后处理选中事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理action bar 的点击响应
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this,
                        SettingsActivity.class);
                startActivity(intent);
                break;
            /**
             * 查询自己的打卡情况
             */
            case R.id.action_view_times:
                viewMyAlertBuilder = new
                        AlertDialog.Builder(MainActivity.this);
                // Set the dialog title and message.
                viewMyAlertBuilder.setTitle(R.string.name_view_times);
                viewMyAlertBuilder.setMessage(R.string.alert_my_title);
                viewMyAlertBuilder.setPositiveButton(R.string.menu_search, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(xh!=null&& !xh.equals("")) {
                                    FillStationParse fillStationParse =
                                            new FillStationParse(1,MainActivity.this);
                                    fillStationParse.execute(xh);
                                }
                            }
                        });
                viewMyAlertBuilder.setNegativeButton(R.string.menu_cancel, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //啥都不干
                            }
                        });
                viewMyAlertBuilder.show();
                break;
            /**
             * 查询别人的打卡情况，插入了自定义view，要求别人输入学号
             */
            case R.id.action_view_other_times:
                viewOthersAlertBuilder = new
                        AlertDialog.Builder(MainActivity.this);
                viewOthersAlertBuilder.setTitle(R.string.name_action_view_other_times);
                viewOthersAlertBuilder.setMessage(R.string.title_acion_view_other_times);
                viewOthersAlertBuilder.setView(R.layout.edit_xh_layout);
                viewOthersAlertBuilder.setPositiveButton(R.string.menu_search, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editXH = (EditText) ((AlertDialog) dialog)
                                        .findViewById(R.id.edit_xh);
                                String xhForChecking = editXH.getText().toString();
                                //检查是否为空，防止app崩溃
                                if(!xhForChecking.equals("")){
                                    FillStationParse fillStationParse =
                                            new FillStationParse(2,MainActivity.this);
                                    fillStationParse.execute(xhForChecking);
                                }else {
                                    Toast.makeText(MainActivity.this,
                                            R.string.toast_xh_noeffect,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                viewOthersAlertBuilder.setNegativeButton(R.string.menu_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                viewOthersAlertBuilder.show();
                break;
            default:

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化notification channel
     */
    public void createNotificationChannel() {
        //在设置里面可以更改notification channel
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "自动打卡后的通知",
                            NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);//zheliyousuobutong
            notificationChannel.enableVibration(false);
            notificationChannel.setDescription
                    ("程序自动每日打卡后的提示");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * 打开background 的service，修改remove task
     */
    public void openBackgroundLoader(){
        //循环读取各个服务，看看我们自定义的服务在里面吗
       if(!isMyServiceRunning(TimingServiceDitry.class)) {
           Log.d(TAG, "openBackgroundLoader: 没有实例，重新启动了一个");
           Intent intent = new Intent(this, TimingServiceDitry.class);
           startService(intent);
           Toast.makeText(this, R.string.switch_texton, Toast.LENGTH_SHORT).show();
           //防止task 被后台杀掉
           Removetask = 1;
       }else {
           Log.d(TAG, "openBackgroundLoader: 有实例，什么都不做");
       }
    }

    /**
     * 关闭background 的service，修改remove task状态码为0,防止其再启动
     */
    public void closeBackgroundLoader(){
        Intent intent = new Intent(this, TimingServiceDitry.class);
        stopService(intent);
        //使restarter可以被移除
        Removetask = 0;
        Toast.makeText(this, R.string.switch_textoff, Toast.LENGTH_SHORT).show();
    }

    /**
     * 有关于保存按钮状态的,get在加载按钮时加载，save 在按钮切换时保存
     * @return 按钮状态，是打开还是关闭
     */
   public boolean getSwitchStation(){
        if(preferences!=null) {
            //Log.d(TAG, "getSwitchStation: 从preference中恢复数据"+isOn);
        }
        return isOn;
   }

    public void saveSwitchStation(Boolean station){
        isOn = station;
    }


    /**忽略电池优化请求 方面代码
     *暂时不用了，因为有了service 保活唤醒
     * @return 是否打开了电池优化，没有就要求打开
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return isIgnoring;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestIgnoreBatteryOptimizations() {
        try {
            @SuppressLint("BatteryLife")
            Intent intent = new Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存开关状态数据
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean("isOn", isOn);
        preferencesEditor.apply();
    }

    /**
     * 查看某个service 是否在后台运行
     * @param serviceClass service所在的类
     * @return 返回是否有service实例
     */
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 处理fab的点击响应
     * @param view
     */
    public void resetSports(View view) {

    }
}