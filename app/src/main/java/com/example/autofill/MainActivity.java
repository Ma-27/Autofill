package com.example.autofill;

import android.annotation.SuppressLint;
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

import com.example.autofill.background.FetchDataAsyncTask;
import com.example.autofill.background.FillStationParse;
import com.example.autofill.background.TimingService;
import com.example.autofill.setting.SettingsActivity;
import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRoomDatabase;
import com.example.autofill.ui.main.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;
import static com.example.autofill.ui.main.MrdkListAdapter.xh;

public class MainActivity extends AppCompatActivity implements GetData{

    private SharedPreferences preferences;
    Boolean isOn = false;
    private static InformationRoomDatabase INSTANCE;
    //通知
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    AlertDialog.Builder viewMyAlertBuilder;
    AlertDialog.Builder viewOthersAlertBuilder;

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

        //开启后台推送
        if(!isIgnoringBatteryOptimizations());{
            requestIgnoreBatteryOptimizations();
        }

        //获取shared preferences
        preferences = getSharedPreferences("switchStation",MODE_PRIVATE);
        isOn = preferences.getBoolean("isOn", false);

        //设置通知channel
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        //初始化notification channel
        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理action bar 的点击响应
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this,
                        SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_view_times:
                viewMyAlertBuilder = new
                        AlertDialog.Builder(MainActivity.this);
                // Set the dialog title and message.
                viewMyAlertBuilder.setTitle(R.string.name_view_times);
                viewMyAlertBuilder.setMessage(R.string.alert_my_title);
                viewMyAlertBuilder.setPositiveButton("查询", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //获取学号并查询
                                if(xh!=""){
                                    FillStationParse fillStationParse = new FillStationParse();
                                    fillStationParse.delegateX1 = MainActivity.this;
                                    fillStationParse.execute(xh);
                                }
                                /*
                                if (INSTANCE == null) {
                                    INSTANCE = InformationRoomDatabase.getDatabase(MainActivity.this);

                                    FetchDataAsyncTask fetchDataAsyncTask = new FetchDataAsyncTask(INSTANCE);
                                    fetchDataAsyncTask.delegateX = MainActivity.this;
                                    fetchDataAsyncTask.execute();
                                }


                                 */
                            }
                        });
                viewMyAlertBuilder.setNegativeButton("取消", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //啥都不干
                            }
                        });
                viewMyAlertBuilder.show();
                break;
            case R.id.action_view_other_times:
                viewOthersAlertBuilder = new
                        AlertDialog.Builder(MainActivity.this);
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
     * 打开或关闭background 的service
     */
    public void openBackgroundLoader(){
        Intent intent = new Intent(this, TimingService.class);
        startService(intent);
    }

    public void closeBackgroundLoader(){
        Intent intent = new Intent(this, TimingService.class);
        stopService(intent);
    }

    /**
     * 有关于保存按钮状态的,get在加载按钮时加载，save 在按钮切换时保存
     * @return
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
     *
     * @return
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
    public void requestIgnoreBatteryOptimizations() {
        try {
            @SuppressLint("BatteryLife")
            Intent intent = new Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean("isOn", isOn);
        preferencesEditor.apply();
    }

    /**
     * 先行放置，待来日再弄从数据库中恢复
     * @param
     */


    //提取数据，这次提取学号
    @Override
    public void onExtractData(List<InformationEntity> informationEntities) {
     /*
        String mSchoolNumber = "";
        InformationEntity current = informationEntities.get(2);
        mSchoolNumber = current.getStation();

        FillStationParse fillStationParse = new FillStationParse();
        fillStationParse.delegateX1 = MainActivity.this;
        fillStationParse.execute(parseStation(mSchoolNumber));
        */
    }



    @Override
    public void onCheckTimesFinish(String times) {
        if(viewMyAlertBuilder!=null){
            viewMyAlertBuilder.setTitle(R.string.name_view_times);
            viewMyAlertBuilder.setMessage("今天打卡"+times+"次");
            viewMyAlertBuilder.show();
        }
    }

    /*
    String parseStation(String unparsedStation) {
        String[] splitted = unparsedStation.split("-");
        return splitted[1];
    }

     */
}