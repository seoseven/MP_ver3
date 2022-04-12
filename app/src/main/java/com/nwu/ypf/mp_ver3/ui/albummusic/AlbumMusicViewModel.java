package com.nwu.ypf.mp_ver3.ui.albummusic;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nwu.ypf.mp_ver3.bean.Album;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;

import java.util.List;

public class AlbumMusicViewModel extends ViewModel implements BaseMusicListViewModel {

    private MutableLiveData<Source<List<IMusicInfo>>> music;

    public AlbumMusicViewModel() {
        music = new MutableLiveData<>(Source.<List<IMusicInfo>>loading());
    }

    void setMusicList(@NonNull Album album) {
        music.setValue(Source.success(album.getMusic()));
    }

    @Override
    public LiveData<Source<List<IMusicInfo>>> getMusicList() {
        return music;
    }
}
