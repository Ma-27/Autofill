package com.example.autofill.background;

import com.example.autofill.storage.InformationEntity;

import org.json.JSONException;

import java.util.List;

public interface Response {
    void onPostFinish(String responseCode);

    void onPostFinish(List<InformationEntity> informationEntities) throws JSONException;
}
