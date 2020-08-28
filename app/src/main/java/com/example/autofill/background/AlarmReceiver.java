package com.example.autofill.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.autofill.network.prepareRequest.FillStationParse;
import com.example.autofill.network.prepareRequest.PositionParse;
import com.example.autofill.network.prepareRequest.PostDataParse;
import com.example.autofill.network.prepareRequest.Response;
import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRoomDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver implements Response {

    private static final String TAG = "AlarmReceiver成功";
    private static InformationRoomDatabase INSTANCE;
    public String mData = "";
    public Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d(TAG, "onReceive: 打开了on receive");

        //获取数据
        if (INSTANCE == null) {
            INSTANCE = InformationRoomDatabase.getDatabase(context);
        }

        //检查网络状态
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        //网络良好就开启打卡服务
        if (networkInfo != null && networkInfo.isConnected()) {
            FetchDataAsyncTask fetchDataAsyncTask = new FetchDataAsyncTask(INSTANCE);
            fetchDataAsyncTask.delegate0 = this;
            fetchDataAsyncTask.execute();
        }else {
            //未知原因错误
            Notify notify2 = new Notify(context,"今日打卡失败","网络连接不良，请重试");
            notify2.deliverNotification();
        }
    }

    /**
     * 推送通知的
     * @param responseCode 返回数据后解析，给出一个状态码，通过匹配状态码推送通知。
     */
    @Override
    public void onPostFinish(String responseCode) {
        //Log.d(TAG, "打开了推送通知"+responseCode);
        switch (responseCode){
            case "REFUSE":
                //服务器拒绝
                Notify notify4 = new Notify(context,"今日打卡失败","服务器拒绝了打卡请求");
                notify4.deliverNotification();
                break;
            case "TODAYNOFILL":
                //今天还没打卡
                PostDataParse postDataParse = new PostDataParse();
                postDataParse.delegate = this;
                postDataParse.execute(mData);
                break;
            case "TODAYHASFILL":
                Notify notify3 = new Notify(context,"今日已经打过卡","本APP不再重复打卡");
                notify3.deliverNotification();
                break;
            case  "OK":
                //打卡成功
                Notify notify1 = new Notify(context,"今日打卡成功！","无需点击此通知");
                notify1.deliverNotification();
                break;
            default:
                //未知原因错误
                Notify notify2 = new Notify(context,"今日打卡失败","未知原因错误，请重试");
                notify2.deliverNotification();
                break;
        }
    }

    //这个是恢复数据后做的
    @Override
    public void onPostFinish(List<InformationEntity> informationEntities) throws JSONException {
        /**
         * 数据整理junior模块
         */
        String mSchoolNumber = "";
        JSONObject jsonJuniorParam = new JSONObject();
        for (int i = 0; i < informationEntities.size(); i++) {
            InformationEntity current = informationEntities.get(i);
            //开始转换json字符串,剔除不应该编码的情况
            if (parseName(current.getStation()).equalsIgnoreCase("图片占位")
                    || parseName(current.getStation()).equalsIgnoreCase("有无咳嗽、乏力、鼻塞、流涕、咽痛、腹泻等症状")) {
            } else {

                jsonJuniorParam.put(
                        parseName(current.getStation()),
                        parseStation(current.getStation())
                );

                if(i == 1){
                    mSchoolNumber =  parseStation(current.getStation());
                }

            }
        }
        /**这里添加一些没有在界面中出现的属性，构成完整的提交字符串
         * 这里默认大家都是健康的
         * 这个增加是有无咳嗽、乏力、鼻塞、流涕、咽痛、腹泻等症状，不让大家填写了
         */

        jsonJuniorParam.put("jbsks", "否");//无肺炎症状
        jsonJuniorParam.put("jbsfl", "否");
        jsonJuniorParam.put("jbsbs", "否");
        jsonJuniorParam.put("jbslt", "否");
        jsonJuniorParam.put("jbsyt", "否");
        jsonJuniorParam.put("jbsfx", "否");

        mData = jsonJuniorParam.toString();
        //检查数据库是否出现错误
        if(mData.length() == 0){
            Notify notify3 = new Notify(context,"今日打卡失败","未能得到有效数据，数据恢复出现问题");
            notify3.deliverNotification();
        }
        //Log.d(TAG, "onPostFinish: 最终json字符串" + mData);

        /**
         * 指令打卡模块，获取地理位置，检查打卡情况
         */
        PositionParse getPosition = new PositionParse();
        getPosition.execute();

        //这里需要判断今天是否已经打过卡，有就不重复打卡
        FillStationParse fillStationParse = new FillStationParse(0);
        fillStationParse.delegate1 = this;
        fillStationParse.execute(mSchoolNumber);
        //Log.d(TAG, "onPostFinish: 打开了查询打卡状况");
    }


    /**
     * 这个用来分裂字符串，将room database里的数据真正转换成你填写的数据
     *
     * @param unparsedStation 存储的原始数据
     * @return 分解完的数据
     */
    String parseStation(String unparsedStation) {
        String[] splitted = unparsedStation.split("-");
        return splitted[1];
    }

    /**
     * 这个用来分裂字符串，获取json object的名字
     *
     * @param unparsedStation 数据库中存储的原始数据
     * @return 数据库中json字符串的名字
     */
    String parseName(String unparsedStation) {
        String[] splitted = unparsedStation.split("-");
        return splitted[0];
    }

}
