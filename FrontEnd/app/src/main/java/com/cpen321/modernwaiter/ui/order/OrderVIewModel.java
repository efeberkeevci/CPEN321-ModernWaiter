package com.cpen321.modernwaiter.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderVIewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OrderVIewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is order fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}