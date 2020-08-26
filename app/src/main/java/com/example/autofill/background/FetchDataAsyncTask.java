package com.example.autofill.background;

import android.os.AsyncTask;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRoomDatabase;

import org.json.JSONException;

import java.util.List;

public class FetchDataAsyncTask extends AsyncTask<Void, Void,List<InformationEntity> >{

    public Response delegate0 = null;
    InformationRoomDatabase db;

    public FetchDataAsyncTask(InformationRoomDatabase instance) {
        db = instance;
    }


    @Override
    protected List<InformationEntity> doInBackground(Void... voids) {
        return db.informationDao().getAllStateInList();
    }

    @Override
    protected void onPostExecute(List<InformationEntity> informationEntities) {
        super.onPostExecute(informationEntities);
        try {
            delegate0.onPostFinish(informationEntities);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

