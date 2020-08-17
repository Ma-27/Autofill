package com.example.autofill.storage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "information_table")

public class InformationTable{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "state")

    private String mState;

    public InformationTable(String state){
        mState = state;
    }

    @NonNull
    public String getStation() {
        return mState;
    }
}
