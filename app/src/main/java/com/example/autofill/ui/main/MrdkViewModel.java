package com.example.autofill.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRepository;

import java.util.List;

/**
 * 复制自information view model并加以改编
 */

public class MrdkViewModel extends AndroidViewModel {

    private InformationRepository informationRepository;
    private LiveData<List<InformationEntity>> mAllData;

    public MrdkViewModel(@NonNull Application application) {
        super(application);
        informationRepository = new InformationRepository(application);
        mAllData = informationRepository.getAllStateInRepository();
    }

    //四个保留的getter 方法，类似repository类 “接口”
    LiveData<List<InformationEntity>> getAllData() {
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