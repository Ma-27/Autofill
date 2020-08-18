package com.example.autofill.storage;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class InformationRepository {

    private InformationDao informationDao;
    private LiveData<List<InformationEntity>> AllState;
    private static final String TAG = "InformationRepository成功";

    //初始化这个类时调用
    public InformationRepository(Application application) {
        InformationRoomDatabase database = InformationRoomDatabase.getDatabase(application);
        informationDao = database.informationDao();
        AllState = informationDao.getAllState();
    }

    //向view model 返回所有数据
    public LiveData<List<InformationEntity>> getAllStateInRepository() {
        Log.d(TAG, "getAllStateInRepository: "+AllState);
        return AllState;
    }


    //又一个会被调用的getter 方法，插入新单词
    public void insert (InformationEntity state) {
        new insertAsyncTask((InformationDao) new insertAsyncTask(informationDao)).execute(state);
    }

    public void deleteAll()  {
        new deleteAllStateAsyncTask((InformationDao) new insertAsyncTask(informationDao)).execute();
    }

    public void deleteWord(InformationEntity state)  {
        new deleteSingleStateAsyncTask((InformationDao) new insertAsyncTask(informationDao)).execute(state);
    }

    private static class insertAsyncTask extends AsyncTask<InformationEntity,Void,Void> {

        private  InformationDao StateDao;

        public insertAsyncTask(InformationDao mWordDao) {
            this.StateDao = mWordDao;
        }

        @Override
        protected Void doInBackground(InformationEntity... informationEntities) {
            //插入单个单词（类名 ... 对象数组）
            StateDao.insert(informationEntities[0]);
            return null;
        }
    }

    private static class deleteAllStateAsyncTask extends AsyncTask<InformationEntity,Void,Void> {

        private  InformationDao StateDao;

        public deleteAllStateAsyncTask(InformationDao mWordDao) {
            this.StateDao = mWordDao;
        }

        @Override
        protected Void doInBackground(InformationEntity... informationEntities) {
            StateDao.deleteAllState();
            return null;
        }
    }

    private static class deleteSingleStateAsyncTask extends AsyncTask<InformationEntity,Void,Void> {

        private  InformationDao StateDao;

        public deleteSingleStateAsyncTask(InformationDao mWordDao) {
            this.StateDao = mWordDao;
        }

        @Override
        protected Void doInBackground(InformationEntity... informationEntities) {
            //插入单个单词（类名 ... 对象数组）
            StateDao.deleteSingleState(informationEntities[0]);
            return null;
        }
    }
}
