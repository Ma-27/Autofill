package com.example.autofill.background;

import android.os.AsyncTask;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRoomDatabase;

public class FetchDataAsyncTask extends AsyncTask<Void, Void, InformationEntity[]>{

    public Response delegate0 = null;
    InformationRoomDatabase db;

    public FetchDataAsyncTask(InformationRoomDatabase instance) {
        db = instance;
    }


    @Override
    protected InformationEntity[] doInBackground(Void... voids) {
        return db.informationDao().getAllStateInList();
    }

    @Override
    protected void onPostExecute(InformationEntity[] informationEntities) {
        super.onPostExecute(informationEntities);
        delegate0.onPostFinish(informationEntities);
    }
}

