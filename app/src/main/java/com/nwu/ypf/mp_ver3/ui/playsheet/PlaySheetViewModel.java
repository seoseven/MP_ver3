package com.nwu.ypf.mp_ver3.ui.playsheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nwu.ypf.mp_ver3.bean.FavoriteMusicInfo;
import com.nwu.ypf.mp_ver3.bean.RecentMusicInfo;
import com.nwu.ypf.mp_ver3.repository.FavoriteMusicRepo;
import com.nwu.ypf.mp_ver3.repository.RecentMusicRepo;

import java.util.List;

public class PlaySheetViewModel extends AndroidViewModel {

    public LiveData<List<RecentMusicInfo>> recentMusic;
    public LiveData<List<FavoriteMusicInfo>> favoriteMusic;

    public PlaySheetViewModel(@NonNull Application application) {
        super(application);
        RecentMusicRepo recentMusicRepo = RecentMusicRepo.getInstance(application);
        recentMusic = recentMusicRepo.getRecentMusic();
        FavoriteMusicRepo favoriteMusicRepo = FavoriteMusicRepo.getInstance(application);
        favoriteMusic = favoriteMusicRepo.getFavoriteMusic();
    }

}
