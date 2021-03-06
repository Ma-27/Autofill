package com.example.autofill.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//这里是用于增删和获取全部数据的dao 类
@Dao
public interface InformationDao {
    //插入单个数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InformationEntity state);

    //获取全部打卡数据
    @Query("SELECT * from information_table")
    LiveData<List<InformationEntity>> getAllState();

    //获取全部打卡数据,不获取live data
    @Query("SELECT * from information_table")
     List<InformationEntity> getAllStateInList();

    //获取单个打卡数据
    @Query("SELECT * from information_table LIMIT 1")
    InformationEntity[] getAnyState();


    //更新单个数据
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSingleState(InformationEntity... information);

    //删除全部的打卡数据
    @Query("DELETE FROM information_table")
    void deleteAllState();


    //删除单个打卡数据
    @Delete
    void deleteSingleState(InformationEntity information);


}
