package com.example.autofill.storage;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRepository;

import java.util.List;

/**
 * 复制自information view model并加以改编
 */

public class InformationViewModel extends AndroidViewModel {

    private InformationRepository informationRepository;
    private LiveData<List<InformationEntity>> mAllData;

    public InformationViewModel(@NonNull Application application) {
        super(application);
        informationRepository = new InformationRepository(application);
        mAllData = informationRepository.getAllStateInRepository();
    }

    //四个保留的getter 方法，类似repository类 “接口”
    public LiveData<List<InformationEntity>> getAllData() {
        return mAllData;
    }

    public void insertSingle(InformationEntity state) {
        informationRepository.insert(state);
    }

    public void deleteAll() {
        informationRepository.deleteAll();
    }

    public void deleteSingle(InformationEntity state) {
        informationRepository.deleteData(state);
    }


    public void updateSingle(InformationEntity state){
        informationRepository.updateSingle(state);
    }

}