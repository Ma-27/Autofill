package com.example.autofill.network.prepareRequest;

import com.example.autofill.storage.InformationEntity;

import org.json.JSONException;

import java.util.List;

public interface Response {
    public void onPostFinish(String responseCode);

    public void onPostFinish(List<InformationEntity> informationEntities) throws JSONException;
}
