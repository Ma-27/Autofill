package com.example.autofill.background;

import com.example.autofill.storage.InformationEntity;

public interface Response {
    void onPostFinish(String responseCode);

    void onPostFinish(InformationEntity[] informationEntities);
}
