package com.example.autofill;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

//直接编写约1818行代码,其中java类1024行,android manifest 33行， layout 761行
public class MainActivity extends AppCompatActivity {

    private static final String TAG =
            MainActivity.class.getSimpleName()+"成功";

    //界面
    EditText m_editAdditionalCondition;
    EditText m_editSchoolNumber;
    EditText m_editName;
    EditText m_editPhoneNumber;
    EditText m_editCurrentLocation;
    EditText m_editDetailedLocation;
    /*
    EditText m_editRoutine;
    EditText m_editTransportation;
    EditText m_editReturnTime;
    EditText m_editIsolationStartTime;
    EditText m_editCuttentWhereabouts;
     */

    protected JSONObject jsonJuniorParam = new JSONObject();

    //存储
    public SharedPreferences mPreferences;
    public static String sharedPrefFile ="com.example.autofill.MainActivity";

    //通知
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private boolean alarmUp;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_editAdditionalCondition = findViewById(R.id.editAdditionalCondition);
        m_editSchoolNumber = findViewById(R.id.editSchoolNumber);
        m_editName = findViewById(R.id.editName);
        m_editPhoneNumber = findViewById(R.id.editPhoneNumber);
        m_editCurrentLocation = findViewById(R.id.editCurrentLocation);
        m_editDetailedLocation = findViewById(R.id.editDetailedLocation);
        /*
        m_editRoutine = findViewById(R.id.editRoutine);
        m_editTransportation = findViewById(R.id.editTransportation);
        m_editReturnTime = findViewById(R.id.editReturnTime);
        m_editIsolationStartTime = findViewById(R.id.editStartIsolationTime);
        m_editCuttentWhereabouts = findViewById(R.id.editCurrentWhereabout);
         */



        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String Schoolnumberstring = mPreferences.getString("SchoolnumberString", "");
        String CurrentLocation = mPreferences.getString("CurrentLocation", "");
        String AdditionalCondition = mPreferences.getString("AdditionalCondition", "");
        String Name = mPreferences.getString("Name", "");
        String PhoneNumber = mPreferences.getString("PhoneNumber", "");
        String DetailedLocation = mPreferences.getString("DetailedLocation", "");
        /*
        String Routine  = mPreferences.getString("Routine","");
        String Transportation  = mPreferences.getString("Transportation","");
        String ReturnTime  = mPreferences.getString("ReturnTime","");
        String IsolationStartTime  = mPreferences.getString("IsolationStartTime","");
        String CuttentWhereabouts  = mPreferences.getString("CuttentWhereabouts","");
         */
        boolean isAlarmup = mPreferences.getBoolean("AlarmOn",true);
        m_editSchoolNumber.setText(Schoolnumberstring);
        m_editName.setText(Name);
        m_editAdditionalCondition.setText(AdditionalCondition);
        m_editPhoneNumber.setText(PhoneNumber);
        m_editCurrentLocation.setText(CurrentLocation);
        m_editDetailedLocation.setText(DetailedLocation);
        /*
        m_editRoutine.setText(Routine);
        m_editTransportation.setText(Transportation);
        m_editReturnTime.setText(ReturnTime);
        m_editIsolationStartTime.setText(IsolationStartTime);
        m_editCuttentWhereabouts.setText(CuttentWhereabouts);
         */


        //开启后台推送
        if(!isIgnoringBatteryOptimizations());{
            requestIgnoreBatteryOptimizations();
        }

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService
                (ALARM_SERVICE);//打开间隔唤醒
        final Intent notifyIntent = new Intent(this,AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this,NOTIFICATION_ID,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //看看之前是不是已经打开了这个app
        Log.d(TAG, "is alarm up"+isAlarmup);
        if(isAlarmup){
            //要是根本没有数据，第一次启动
            alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                    notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        }else {
            alarmUp = isAlarmup;
        }
        ToggleButton alarmToggle = findViewById(R.id.button);//找到定义好的按钮
        Log.d(TAG, "alarmup"+alarmUp);
        alarmToggle.setChecked(alarmUp);
        //按钮的触发处理函数
        alarmToggle.setOnCheckedChangeListener(
                //这里是按下按钮后执行的
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {
                        Log.d(TAG, "成功ischecked"+isChecked);
                        String toastMessage;
                        if(!isChecked){
                            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                            preferencesEditor.putString("JSONSeniorString",EncodeJSONSeniorParam());
                            preferencesEditor.putString("CurrentLocation",m_editCurrentLocation.getText().toString());
                            preferencesEditor.putString("SchoolnumberString",m_editSchoolNumber.getText().toString());
                            preferencesEditor.apply();
                            //设置间隔启动
                            long repeatInterval =  //1;//一分钟
                                    AlarmManager.INTERVAL_HALF_DAY;
                            long triggerTime = SystemClock.elapsedRealtime()
                                    + repeatInterval;
                            if (alarmManager != null) {
                                alarmManager.setInexactRepeating
                                        (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                                triggerTime, repeatInterval,
                                                notifyPendingIntent);
                            }
                            AlarmReceiver mAlarmReceiver = new AlarmReceiver();
                            mAlarmReceiver.onReceive(MainActivity.this,notifyIntent);
                            toastMessage = "开启自动打卡!";
                        } else {
                            mNotificationManager.cancelAll();
                            if (alarmManager != null) {
                                alarmManager.cancel(notifyPendingIntent);
                            }
                            toastMessage = "关闭自动打卡!";
                        }
                        //发送toast.
                        Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_SHORT)
                                .show();
                        //保存启动设置
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        preferencesEditor.putBoolean("AlarmOn",isChecked);
                        preferencesEditor.apply();
                        Log.d(TAG, "切换设置前的boolean"+isChecked);
                    }
                });
        createNotificationChannel();
    }

    //不允许更改public属性！！！！！！
    public void onJudgeButtonClicked(View view) {
        try {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.yes1:
                    if (checked){
                        jsonJuniorParam.put("hjsfly","是");}//已离开cq
                    break;
                case R.id.yes2:
                    if (checked){
                        jsonJuniorParam.put("sfyfy","是");
                    } //已返回cq
                    break;
                //这底下是check no的
                case R.id.yes3:
                    if (checked){
                        jsonJuniorParam.put("twsfzc","是");}//体温正常
                    break;
                case R.id.yes4:
                    if (checked){
                        jsonJuniorParam.put("brsfqz","有");}//本人和家人是确诊
                    break;
                case R.id.yes5:
                    if(checked){
                        jsonJuniorParam.put("brsfys","有");}//本人和家人是疑似
                    break;
                case R.id.no1:
                    if (checked){
                        jsonJuniorParam.put("hjsfly","否");}//未离开cq
                    break;
                case R.id.no2:
                    if (checked) {
                        jsonJuniorParam.put("sfyfy", "否");
                        EncodeJsonJuniorParamNo();}//未返回cq
                    break;
                case R.id.no3:
                    if (checked){
                        jsonJuniorParam.put("twsfzc","否");}//体温不正常
                    break;
                case R.id.no4:
                    if (checked){
                        jsonJuniorParam.put("brsfqz","无");}//本人和家人不是确诊
                    break;
                case R.id.no5:
                    if (checked){
                        jsonJuniorParam.put("brsfys","无");}//本人和家人不是疑似
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //不允许更改public属性！！！！！！
    public void onJudge1ButtonClicked(View view) {
        try {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.have1:
                    if (checked) {
                        jsonJuniorParam.put("ywjchblj","有");
                    }//接触hb
                    break;
                case R.id.have2:
                    if (checked)
                    {
                        jsonJuniorParam.put("ywjcqzbl","有");//有无接触确诊病例
                    }//接触确诊
                    break;
                case R.id.have3:
                    if (checked){
                        jsonJuniorParam.put("jbsks","是");//本人和家人是确诊
                        jsonJuniorParam.put("jbsfl","否");
                        jsonJuniorParam.put("jbsbs","否");
                        jsonJuniorParam.put("jbslt","否");
                        jsonJuniorParam.put("jbsyt","否");
                        jsonJuniorParam.put("jbsfx","否");}
                    break;
                case R.id.have4:
                    if (checked){
                        jsonJuniorParam.put("sfyfy","是");}//有疾病史
                    break;
                case R.id.have5:
                    if (checked){
                        jsonJuniorParam.put("xjzdywqzbl","有");}//现居地有患者
                    break;
                case R.id.not_have1:
                    if (checked){
                        jsonJuniorParam.put("ywjchblj","无");
                    }//未接触hb人
                    break;
                case R.id.not_have2:
                    if (checked){
                        jsonJuniorParam.put("ywjcqzbl","无");//有无接触确诊病例
                    }//未接触确诊
                    break;
                case R.id.not_have3:
                    if (checked){
                        jsonJuniorParam.put("jbsks","否");//无肺炎症状
                        jsonJuniorParam.put("jbsfl","否");
                        jsonJuniorParam.put("jbsbs","否");
                        jsonJuniorParam.put("jbslt","否");
                        jsonJuniorParam.put("jbsyt","否");
                        jsonJuniorParam.put("jbsfx","否");}
                    break;
                case R.id.not_have4:
                    if (checked){
                        jsonJuniorParam.put("sfyfy","否");}//无疾病史
                    break;
                case R.id.not_have5:
                    if (checked){
                        jsonJuniorParam.put("xjzdywqzbl","无");}//现居地无患者了
                    break;
                default:// Do nothing.
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private String EncodeJSONSeniorParam() {
        try {
            if(TextUtils.isEmpty(m_editAdditionalCondition.getText())){
                jsonJuniorParam.put("beizhu","填写其他需要说明的情况");
            }else {
                jsonJuniorParam.put("beizhu",m_editAdditionalCondition.getText().toString());
            }
            /*
            if(TextUtils.isEmpty(m_editRoutine.getText())){
                jsonJuniorParam.put("xb","");
            }else {
                jsonJuniorParam.put("xb",m_editRoutine.getText().toString());
            }
            if(TextUtils.isEmpty(m_editTransportation.getText())){
                jsonJuniorParam.put("fyjtgj", "无");//返渝交通工具
            }else {
                jsonJuniorParam.put("fyjtgj",m_editTransportation.getText().toString());
            }
            if(TextUtils.isEmpty(m_editReturnTime.getText())){
                jsonJuniorParam.put("fyddsj", "无");//返渝时间
            }else {
                jsonJuniorParam.put("fyddsj",m_editReturnTime.getText().toString());
            }
            if(TextUtils.isEmpty(m_editIsolationStartTime.getText())){
                jsonJuniorParam.put("jjglqssj", "无");//居家隔离起始时间
            }else {
                jsonJuniorParam.put("jjglqssj",m_editIsolationStartTime.getText().toString());
            }
            if(TextUtils.isEmpty(m_editCuttentWhereabouts.getText())){
                jsonJuniorParam.put("wjjglmqqx", "无");//为居家隔离目前去向
            }else {
                jsonJuniorParam.put("wjjglmqqx",m_editCuttentWhereabouts.getText().toString());
            }
             */

            jsonJuniorParam.put("sign","c4cb4738a0b923820scc509a6f75849b");
            jsonJuniorParam.put("xh",m_editSchoolNumber.getText().toString());
            jsonJuniorParam.put("name",m_editName.getText().toString());
            jsonJuniorParam.put("lxdh",m_editPhoneNumber.getText().toString());
            jsonJuniorParam.put("szdq",m_editCurrentLocation.getText().toString());
            jsonJuniorParam.put("xxdz",m_editDetailedLocation.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonJuniorParam.toString();
    }

    /*
    public void onJudge2ButtonClicked(View view) {
        try {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.newyes1:
                    if (checked) {
                        jsonJuniorParam.put("sfbgsq", "有");//是报告社区
                    }//接触hb
                    break;
                case R.id.newno1:
                    if (checked) {
                        jsonJuniorParam.put("sfbgsq", "无");//否报告社区
                    }//接触hb
                    break;
                case R.id.newyes2:
                    if (checked) {
                        jsonJuniorParam.put("sfjjgl", "有");//是居家隔离
                    }//接触hb
                    break;
                case R.id.newno2:
                    if (checked) {
                        jsonJuniorParam.put("sfjjgl", "无");//不是居家隔离
                    }//接触hb
                    break;
                default:{
                    jsonJuniorParam.put("sfbgsq", "无");//否报告社区
                    jsonJuniorParam.put("sfjjgl", "无");//不是居家隔离
                }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    private void EncodeJsonJuniorParamNo(){
        try {
            jsonJuniorParam.put("ywytdzz", "无");
            jsonJuniorParam.put("fyjtgj", "无");//返渝交通工具
            jsonJuniorParam.put("fyddsj", "无");//返渝时间
            jsonJuniorParam.put("sfbgsq", "无");//否报告社区
            jsonJuniorParam.put("sfjjgl", "无");//不是居家隔离
            jsonJuniorParam.put("jjglqssj", "无");//居家隔离起始时间
            jsonJuniorParam.put("wjjglmqqx", "无");//为居家隔离目前去向
            jsonJuniorParam.put("qtycqk", "无");
            jsonJuniorParam.put("xb", "");//相伴
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("SchoolnumberString",m_editSchoolNumber.getText().toString());
        preferencesEditor.putString("CurrentLocation",m_editCurrentLocation.getText().toString());
        preferencesEditor.putString("AdditionalCondition",m_editAdditionalCondition.getText().toString());
        preferencesEditor.putString("Name",m_editName.getText().toString());
        preferencesEditor.putString("PhoneNumber",m_editPhoneNumber.getText().toString());
        preferencesEditor.putString("DetailedLocation",m_editDetailedLocation.getText().toString());
        preferencesEditor.putString("JSONSeniorString",EncodeJSONSeniorParam());

        /*
        preferencesEditor.putString("Routine",m_editRoutine.getText().toString());
        preferencesEditor.putString("Transportation",m_editTransportation.getText().toString());
        preferencesEditor.putString("ReturnTime",m_editReturnTime.getText().toString());
        preferencesEditor.putString("Transportation",m_editTransportation.getText().toString());
        preferencesEditor.putString("IsolationStartTime",m_editIsolationStartTime.getText().toString());
        preferencesEditor.putString("CuttentWhereabouts",m_editCuttentWhereabouts.getText().toString());
*/
        preferencesEditor.apply();
    }

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

}
