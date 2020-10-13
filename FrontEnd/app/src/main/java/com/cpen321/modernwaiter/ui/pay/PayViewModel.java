package com.cpen321.modernwaiter.ui.pay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PayViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pay fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}