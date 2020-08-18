package com.example.autofill.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

//这里是用于增删和获取全部数据的dao 类
@Dao
public interface InformationDao {
    //插入单个状况
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InformationEntity state);

    //删除全部填写的打卡数据
    @Query("DELETE FROM information_table")
    void deleteAllState();

    //获取全部打卡数据
    @Query("SELECT * from information_table")
    LiveData<List<InformationEntity>> getAllState();

    //获取单个打卡数据
    @Query("SELECT * from information_table LIMIT 1")
    InformationEntity[] getAnyState();

    //删除单个打卡数据
    @Delete
    void deleteSingleState(InformationEntity information);
}
