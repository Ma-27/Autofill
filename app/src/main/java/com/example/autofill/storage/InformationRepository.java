package com.example.autofill.storage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class InformationRepository {

    private InformationDao informationDao;
    private LiveData<List<InformationTable>> AllState;

    //初始化这个类时调用
    public InformationRepository(Application application) {
        InformationRoomDatabase database = InformationRoomDatabase.getDatabase(application);
        informationDao = database.informationDao();
        AllState = informationDao.getAllState();
    }

    //向view model 返回所有数据
    public LiveData<List<InformationTable>> getAllStateInRepository() {
        return AllState;
    }


    //又一个会被调用的getter 方法，插入新单词
    public void insert (InformationTable state) {
        new insertAsyncTask((InformationDao) new insertAsyncTask(informationDao)).execute(state);
    }

    public void deleteAll()  {
        new deleteAllStateAsyncTask((InformationDao) new insertAsyncTask(informationDao)).execute();
    }

    public void deleteWord(InformationTable state)  {
        new deleteSingleStateAsyncTask((InformationDao) new insertAsyncTask(informationDao)).execute(state);
    }

    private static class insertAsyncTask extends AsyncTask<InformationTable,Void,Void> {

        private  InformationDao StateDao;

        public insertAsyncTask(InformationDao mWordDao) {
            this.StateDao = mWordDao;
        }

        @Override
        protected Void doInBackground(InformationTable... informationTables) {
            //插入单个单词（类名 ... 对象数组）
            StateDao.insert(informationTables[0]);
            return null;
        }
    }

    private static class deleteAllStateAsyncTask extends AsyncTask<InformationTable,Void,Void> {

        private  InformationDao StateDao;

        public deleteAllStateAsyncTask(InformationDao mWordDao) {
            this.StateDao = mWordDao;
        }

        @Override
        protected Void doInBackground(InformationTable... informationTables) {
            StateDao.deleteAllState();
            return null;
        }
    }

    private static class deleteSingleStateAsyncTask extends AsyncTask<InformationTable,Void,Void> {

        private  InformationDao StateDao;

        public deleteSingleStateAsyncTask(InformationDao mWordDao) {
            this.StateDao = mWordDao;
        }

        @Override
        protected Void doInBackground(InformationTable... informationTables) {
            //插入单个单词（类名 ... 对象数组）
            StateDao.deleteSingleState(informationTables[0]);
            return null;
        }
    }
}
