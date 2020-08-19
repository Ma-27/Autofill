package com.example.autofill.ui.main;

import android.util.Log;

public class MrdkCacheHolder {
    private String title;
    private String info;
    private String data;
    private static final String TAG = "MrdkCacheHolder成功";
    /**
     * 缓存数据的构造器
     * @param title 设置中recycler view标题
     * @param info 设置中recycler view 保存的数据，不是entity中的数据，经过处理
     */
    public MrdkCacheHolder(String title,String info,String data){
        this.title = title;
        this.info = info;
        this.data = data;
        //Log.d(TAG, "MrdkCacheHolder: ");
    }

    String getTitle() {
        return title;
    }

    String getInfo(){
        return info;
    }

    String getData(){
        return data;
    }
}
