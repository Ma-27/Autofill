package com.example.autofill.storage;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.autofill.R;

@Database(entities = {InformationEntity.class}, version = 1, exportSchema = false)

/**
 * 将该类注释为Room数据库。这个是dao~sqlite~entity的集合
 * 声明属于数据库的实体-在这种情况下，只有一个实体Word
 * @param exportSchema 保留模式版本的历史记录。为此，您可以禁用它，因为您不迁移数据库。
 */
public abstract class InformationRoomDatabase extends RoomDatabase {

    //TODO:定义与数据库一起使用的DAO。为每个@Dao提供一个abstract的“getter”方法。
    // 从repository 和构建word数据的async task中访问这个方法
    public abstract InformationDao informationDao();

    public static InformationRoomDatabase INSTANCE;

    public static InformationRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (InformationRoomDatabase.class) {
                if (INSTANCE == null) {
                    // TODO: 创建一个初始的数据库
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            InformationRoomDatabase.class,"data_database")
                            //添加回调，初始化时添加数据，这个项目中没有启用 migration功能
                            .addCallback(informationRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static RoomDatabase.Callback informationRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }

    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {

        private final InformationDao stateInformationDao;

        //初始化打卡数据
        String[] information = {
                "xh-20xx~xxx~xxx",
                "name-小明",
                "lxdh-1xx~xxx~xxxx",//联系电话
                "szdq-重庆市，重庆市，南岸区",
                "xxdz-重庆邮电大学",
                "hjsfly-待选择",//假期是否离开重庆
                "ywjchblj-待选择",//暑假期间有无接触湖北旅居人员
                "ywjcqzbl-待选择",//暑假期间有无接触确诊或疑似病例
                "xjzdywqzbl-待选择",//现居住地有无确诊或疑似病例
                "twsfzc-待选择",//体温是否正常
                "有无咳嗽、乏力、鼻塞、流涕、咽痛、腹泻等症状-待选择",//有无咳嗽、乏力、鼻塞、流涕、咽痛、腹泻等症状
                "brsfqz-待选择",//本人及家人是否为确诊病例
                "brsfys-待选择",//本人及家人是否为疑似病例
                "jbs-待选择",//有无疾病史
                "beizhu-填写其他需要说明的情况",//备注
        };

        public PopulateDbAsync(InformationRoomDatabase instance) {
            stateInformationDao = instance.informationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           //当启动时，填充初始化数据
            //stateInformationDao.deleteAll();
            if(stateInformationDao.getAnyState().length < 1){
                for (int i = 0; i <= information.length - 1; i++) {
                    InformationEntity data = new InformationEntity(information[i]);
                    //插入单个打卡数据
                    stateInformationDao.insert(data);
                }
            }
            return null;
        }
    }
}
