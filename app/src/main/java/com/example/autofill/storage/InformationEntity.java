package com.example.autofill.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName =  "information_table")
public class InformationEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "state")
    String mState;

    public InformationEntity(@NonNull String state){
        this.mState = state;
    }

    //这个数据不添加进数据库
    @Ignore
    public InformationEntity(@NonNull String state,int id){
        this.mState = state;
        this.id = id;
    }

    @NonNull
    public String getStation() {
        return this.mState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
