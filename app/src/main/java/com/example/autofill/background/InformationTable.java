package com.example.autofill.background;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "information_table")
public class InformationTable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "state")

    private String mState;

    public InformationTable(String state){
        mState = state;
    }

    @NonNull
    public String getmStation() {
        return mState;
    }
}
