package com.nwu.ypf.mp_ver3.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.repository.MusicRepo;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;

import java.util.List;

public class SearchResultViewModel extends AndroidViewModel implements BaseMusicListViewModel {

    private MutableLiveData<Source<List<IMusicInfo>>> _music;
    public LiveData<Source<List<IMusicInfo>>> music;

    public SearchResultViewModel(@NonNull Application application) {
        super(application);
        _music = new MutableLiveData<>(Source.<List<IMusicInfo>>loading());
        music = _music;
    }

    @Override
    public LiveData<Source<List<IMusicInfo>>> getMusicList() {
        return music;
    }

    public void performSearch(@Nullable String text) {
        MusicRepo.ScanLocalMusicCallback callback = new MusicRepo.ScanLocalMusicCallback() {
            @Override
            public void onFinish(@Nullable List<IMusicInfo> music) {
                _music.postValue(Source.success(music));
            }
        };
        MusicRepo.getInstance().scanLocalMusic(getApplication(), 0L, 0L, text,
                callback);
    }
}
