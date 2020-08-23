package com.example.autofill.ui.main;

public class MrdkCacheHolder {
    private String title;
    private String dataName;
    private int visibility;
    private int imageResource;
    private String contentHint;
    private String dataYes;
    private String dataNo;
    private static final String TAG = "MrdkCacheHolder成功";
    /**
     * 缓存数据的构造器
     * @param title 界面中recycler view标题
     * @param content json字符串中的 每个数据的名称
     */
    public MrdkCacheHolder(String title,
                           String dataName,
                           int visibility,
                           int imageResource,
                           String contentHint,
                           String dataYes,
                           String dataNo
    ){
        this.title = title;
        this.dataName = dataName;
        this.visibility = visibility;
        this.imageResource = imageResource;
        this.contentHint = contentHint;
        this.dataYes = dataYes;
        this.dataNo = dataNo;

        //Log.d(TAG, "MrdkCacheHolder: ");
    }

    String getTitle() {
        return title;
    }

    String getDataName(){
        return dataName;
    }

    int getVisibility(){
        //Log.d(TAG, "getVisibility:看看 "+visibility);
        return visibility;
    }

    int getImage(){
        return imageResource;
    }

    String getContentHint(){
        return contentHint;
    }

    String getDataYes(){
        return dataYes;
    }

    String getDataNo(){
        return dataNo;
    }
}
