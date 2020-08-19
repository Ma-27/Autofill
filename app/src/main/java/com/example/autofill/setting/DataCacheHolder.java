package com.example.autofill.setting;

class DataCacheHolder {

    private String title;
    private String info;
    private String data;
    /**
     * 缓存数据的构造器
     * @param title 设置中recycler view标题
     * @param info 设置中recycler view 保存的数据，不是entity中的数据，经过处理
     */
    public DataCacheHolder(String title,String info,String data){
        this.title = title;
        this.info = info;
        this.data = data;
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
