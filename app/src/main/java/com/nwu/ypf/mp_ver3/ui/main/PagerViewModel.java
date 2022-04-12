package com.nwu.ypf.mp_ver3.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PagerViewModel extends ViewModel {

    private MutableLiveData<Boolean> _showSearchResult;
    public LiveData<Boolean> showSearchResult;

    public PagerViewModel() {
        _showSearchResult = new MutableLiveData<>(false);
        showSearchResult = _showSearchResult;
    }

    public void showSearchResult() {
        _showSearchResult.postValue(true);
    }

    public void destroySearchResult() {
        _showSearchResult.postValue(false);
    }

}
