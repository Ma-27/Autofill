package com.example.autofill.storage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "information_table")
public class InformationEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "state")
    String mState;

    public InformationEntity(@NonNull String state){
        this.mState = state;
    }

    @NonNull
    public String getStation() {
        return this.mState;
    }
}
