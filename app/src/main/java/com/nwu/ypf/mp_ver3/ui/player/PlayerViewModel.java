package com.nwu.ypf.mp_ver3.ui.player;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nwu.ypf.mp_ver3.bean.FavoriteMusicInfo;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.repository.FavoriteMusicRepo;
import com.nwu.ypf.mp_ver3.util.PlayManager;

public class PlayerViewModel extends AndroidViewModel {

    private FavoriteMusicRepo favoriteMusicRepo;
    private LiveData<IMusicInfo> playingMusic;
    /**
     * 尝试去喜欢的数据库查询正在播放的歌曲。若已经添加喜欢则不为 null.
     */
    private LiveData<FavoriteMusicInfo> favoriteMusicInfo;
    public LiveData<Boolean> isFavorite;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        favoriteMusicRepo = FavoriteMusicRepo.getInstance(application);
        playingMusic = PlayManager.getInstance(application).playingMusic;

        favoriteMusicInfo = Transformations.switchMap(playingMusic,
                new Function<IMusicInfo, LiveData<FavoriteMusicInfo>>() {
                    @Override
                    public LiveData<FavoriteMusicInfo> apply(IMusicInfo input) {
                        if (input != null) {
                            return favoriteMusicRepo.queryFavoriteMusic(input.getId());
                        } else {
                            // 未在播放音乐
                            return new MutableLiveData<>();
                        }
                    }
                });

        isFavorite = Transformations.switchMap(favoriteMusicInfo,
                new Function<FavoriteMusicInfo, LiveData<Boolean>>() {
                    @Override
                    public LiveData<Boolean> apply(FavoriteMusicInfo input) {
                        return new MutableLiveData<>(input != null);
                    }
                });
    }

    /**
     * 切换正在播放歌曲的喜欢状态。
     */
    public void toggleFavorite() {
        IMusicInfo m = playingMusic.getValue();
        Boolean isF = isFavorite.getValue();
        if (m != null) {
            if (isF == null || !isF) {
                favoriteMusicRepo.upsert(new FavoriteMusicInfo(m));
            } else {
                favoriteMusicRepo.removeFavorite(m.getId());
            }
        }
    }


}
