package com.example.autofill.storage;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class InformationRepository {

    private InformationDao informationDao;
    private LiveData<List<InformationTable>> AllInformation;

    //初始化这个类时调用
    InformationRepository(Application application){
        InformationRoomDatabase database = InformationRoomDatabase.getDatabase(application);
        informationDao = database.informationDao();
        AllInformation = informationDao.getAllWords();
    }

    //向view model 返回所有数据
    LiveData<List<InformationTable>> getAllWords() {
        return AllInformation;
    }


}
