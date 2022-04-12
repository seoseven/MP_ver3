package com.nwu.ypf.mp_ver3.ui.singermusic;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Singer;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;

import java.util.List;

public class SingerMusicViewModel extends ViewModel implements BaseMusicListViewModel {

    private MutableLiveData<Source<List<IMusicInfo>>> music;

    public SingerMusicViewModel() {
        music = new MutableLiveData<>(Source.<List<IMusicInfo>>loading());
    }

    void setMusicList(@NonNull Singer singer) {
        music.setValue(Source.success(singer.getMusic()));
    }

    @Override
    public LiveData<Source<List<IMusicInfo>>> getMusicList() {
        return music;
    }
}
