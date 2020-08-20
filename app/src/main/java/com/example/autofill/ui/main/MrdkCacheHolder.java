package com.example.autofill.ui.main;

import android.util.Log;

public class MrdkCacheHolder {
    private String title;
    private String content;
    private int visibility;
    private final int imageResource;
    private static final String TAG = "MrdkCacheHolder成功";
    /**
     * 缓存数据的构造器
     * @param title 界面中recycler view标题
     * @param content json字符串中的 每个数据的名称
     */
    public MrdkCacheHolder(String title,
                           String content,
                           int visibility,
                           int imageResource){
        this.title = title;
        this.content = content;
        this.visibility = visibility;
        this.imageResource = imageResource;
        //Log.d(TAG, "MrdkCacheHolder: ");
    }

    String getTitle() {
        return title;
    }

    String getContent(){
        return content;
    }

    int getVisibility(){
        //Log.d(TAG, "getVisibility:看看 "+visibility);
        return visibility;
    }

    int getImage(){
        return imageResource;
    }
}
