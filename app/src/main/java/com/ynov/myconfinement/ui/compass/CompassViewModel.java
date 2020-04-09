package com.ynov.myconfinement.ui.compass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompassViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CompassViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Compass fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}