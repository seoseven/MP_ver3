package com.nwu.ypf.mp_ver3.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nwu.ypf.mp_ver3.bean.Album;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Singer;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.repository.MusicRepo;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;
import com.nwu.ypf.mp_ver3.util.LocalMusicLiveData;

import java.util.List;

/**
 * 这个类的存在让所有歌曲信息都是同步的
 */

public class SharedViewModel extends AndroidViewModel implements BaseMusicListViewModel {

    private MusicRepo repo;

    public final LiveData<Source<List<IMusicInfo>>> localMusicList;

    public final LiveData<Source<List<Singer>>> singerList;
    public final LiveData<Source<List<Album>>> albumList;

    /**
     * 用户在歌手列表中所点击的歌手，用于 {@link com.nwu.ypf.mp_ver3.ui.singermusic.SingerMusicFr} 的参数
     * 传递。
     */
    private Singer currentSinger;

    /**
     * 用户在专辑列表中所点击的专辑，用于 {@link com.nwu.ypf.mp_ver3.ui.albummusic.AlbumMusicFr} 的参数
     * 传递。
     */
    private Album currentAlbum;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        repo = MusicRepo.getInstance();

        localMusicList = Transformations.map(new LocalMusicLiveData(application, repo),
                new Function<List<IMusicInfo>, Source<List<IMusicInfo>>>() {
                    @Override
                    public Source<List<IMusicInfo>> apply(List<IMusicInfo> input) {
                        if (input == null) {
                            return Source.loading();
                        } else {
                            return Source.success(input);
                        }
                    }
                });

        singerList = Transformations.switchMap(localMusicList, new Function<Source<List<IMusicInfo>>,
                LiveData<Source<List<Singer>>>>() {
            @Override
            public LiveData<Source<List<Singer>>> apply(Source<List<IMusicInfo>> input) {
                switch (input.getState()) {
                    case Loading:
                        return new MutableLiveData<>(Source.<List<Singer>>loading());
                    case Success:
                        List<IMusicInfo> music = input.getData();
                        if (music != null) {
                            return repo.groupMusicWithSinger(music);
                        } else {
                            return new MutableLiveData<>(Source.<List<Singer>>failed());
                        }
                    case Failed:
                        return new MutableLiveData<>(Source.<List<Singer>>failed());
                }
                throw new RuntimeException("Unknown state");

            }
        });

        albumList = Transformations.switchMap(localMusicList, new Function<Source<List<IMusicInfo>>,
                LiveData<Source<List<Album>>>>() {
            @Override
            public LiveData<Source<List<Album>>> apply(Source<List<IMusicInfo>> input) {
                switch (input.getState()) {
                    case Loading:
                        return new MutableLiveData<>(Source.<List<Album>>loading());
                    case Success:
                        List<IMusicInfo> music = input.getData();
                        if (music != null) {
                            return repo.groupMusicWithAlbum(music);
                        } else {
                            return new MutableLiveData<>(Source.<List<Album>>failed());
                        }
                    case Failed:
                        return new MutableLiveData<>(Source.<List<Album>>failed());
                }
                throw new RuntimeException("Unknown state");
            }
        });

    }

    @Override
    public LiveData<Source<List<IMusicInfo>>> getMusicList() {
        return localMusicList;
    }

    public Singer getCurrentSinger() {
        return currentSinger;
    }

    public void setCurrentSinger(Singer currentSinger) {
        this.currentSinger = currentSinger;
    }

    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }
}
