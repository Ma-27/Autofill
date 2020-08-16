package com.example.autofill.background;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public abstract class InformationRoomDatabase extends RoomDatabase {

    //定义与数据库一起使用的DAO。为每个@Dao提供一个abstract的“getter”方法。
    //从repository 和构建word数据的async task中访问这个方法
    public abstract InformationDao informationDao();

    public static InformationRoomDatabase INSTANCE;

    public static InformationRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (InformationRoomDatabase.class) {
                if (INSTANCE == null) {
                    // TODO: 创建一个初始的数据库
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            InformationRoomDatabase.class,"word_database")
                            //添加回调，初始化时添加数据，这个项目中没有启用 migration功能
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }

    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {

        private final InformationDao myInformationDao;

        //有待填充
        String[] words = {};

        public PopulateDbAsync(InformationRoomDatabase instance) {
            myInformationDao = instance.informationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            /**
           当启动时，填充初始化数据
             */
            //myWordDao.deleteAll();
            if(myInformationDao.getAnyWord().length < 1){
                for (int i = 0; i <= words.length - 1; i++) {
                    InformationTable data = new InformationTable(words[i]);
                    //插入单个单词
                    myInformationDao.insert(data);
                }
            }

            return null;
        }
    }
}
