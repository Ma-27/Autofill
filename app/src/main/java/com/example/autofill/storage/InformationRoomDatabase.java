package com.example.autofill.storage;

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
        String[] information = {
                "1000-000-000",
                "小明",
                "1xx-xxx-xxxx",//联系电话
                "重庆市，重庆市，南岸区",
                "重庆邮电大学",
                "待选择",//假期是否离开重庆
                "待选择",//暑假期间有无接触湖北旅居人员
                "待选择",//暑假期间有无接触确诊或疑似病例
                "待选择",//现居住地有无确诊或疑似病例
                "待选择",//体温是否正常
                "待选择",//有无咳嗽、乏力、鼻塞、流涕、咽痛、腹泻等症状
                "待选择",//本人及家人是否为确诊病例
                "待选择",//本人及家人是否为疑似病例
                "待选择",//有无疾病史
                "填写其他需要说明的情况",//备注
        };

        public PopulateDbAsync(InformationRoomDatabase instance) {
            myInformationDao = instance.informationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           //当启动时，填充初始化数据
            //myInformationDao.deleteAll();
            if(myInformationDao.getAnyWord().length < 1){
                for (int i = 0; i <= information.length - 1; i++) {
                    InformationTable data = new InformationTable(information[i]);
                    //插入单个打卡数据
                    myInformationDao.insert(data);
                }
            }

            return null;
        }
    }
}
