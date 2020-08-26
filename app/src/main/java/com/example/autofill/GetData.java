package com.example.autofill;

import com.example.autofill.storage.InformationEntity;

import java.util.List;

public interface GetData {
    void onExtractData(List<InformationEntity> informationEntities);

    void onCheckTimesFinish(String times);
}
