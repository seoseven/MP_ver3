package com.nwu.ypf.mp_ver3.ui.recentmusic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.RecentMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.repository.RecentMusicRepo;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecentMusicViewModel extends AndroidViewModel implements BaseMusicListViewModel {

    private RecentMusicRepo recentMusicRepo;
    private LiveData<Source<List<IMusicInfo>>> music;

    public RecentMusicViewModel(@NonNull Application application) {
        super(application);
        recentMusicRepo = RecentMusicRepo.getInstance(application);

        music = Transformations.map(recentMusicRepo.getRecentMusic(),
                new Function<List<RecentMusicInfo>, Source<List<IMusicInfo>>>() {
                    @Override
                    public Source<List<IMusicInfo>> apply(List<RecentMusicInfo> input) {
                        if (input == null) {
                            return Source.loading();
                        } else {
                            List<IMusicInfo> tmp = new ArrayList<>();
                            tmp.addAll(input);
                            return Source.success(tmp);
                        }
                    }
                });
    }

    @Override
    public LiveData<Source<List<IMusicInfo>>> getMusicList() {
        return music;
    }
}
