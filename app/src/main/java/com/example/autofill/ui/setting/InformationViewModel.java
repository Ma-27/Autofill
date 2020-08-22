package com.example.autofill.ui.setting;
import	java.util.List;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.autofill.storage.InformationEntity;
import com.example.autofill.storage.InformationRepository;

//ViewModel充当存储库和UI之间的通信中心。
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

    public void insert(InformationEntity state) {
        informationRepository.insert(state);
    }

    public void deleteAll() {informationRepository.deleteAll();}

    public void deleteWord(InformationEntity state) {informationRepository.deleteData(state);}

}
