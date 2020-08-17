package com.example.autofill.setting;
import	java.util.List;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.autofill.storage.InformationRepository;
import com.example.autofill.storage.InformationTable;

public class InformationViewModel extends AndroidViewModel {

    private InformationRepository informationRepository;
    private LiveData<List<InformationTable>> mAllData;

    public InformationViewModel(@NonNull Application application) {
        super(application);
        informationRepository = new InformationRepository(application);
        mAllData = informationRepository.getAllStateInRepository();
    }



    //四个保留的getter 方法，类似repository类 “接口”
    LiveData<List<InformationTable>> getAllData() {
        return mAllData;
    }

    public void insert(InformationTable state) {
        informationRepository.insert(state);
    }

    public void deleteAll() {informationRepository.deleteAll();}

    public void deleteWord(InformationTable state) {informationRepository.deleteWord(state);}

}
