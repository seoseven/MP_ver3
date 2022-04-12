package com.nwu.ypf.mp_ver3.ui.favoritemusic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.nwu.ypf.mp_ver3.bean.FavoriteMusicInfo;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.repository.FavoriteMusicRepo;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMusicViewModel extends AndroidViewModel implements BaseMusicListViewModel {

    private FavoriteMusicRepo favoriteMusicRepo;
    private LiveData<Source<List<IMusicInfo>>> music;

    public FavoriteMusicViewModel(@NonNull Application application) {
        super(application);
        favoriteMusicRepo = FavoriteMusicRepo.getInstance(application);

        music = Transformations.map(favoriteMusicRepo.getFavoriteMusic(),
                new Function<List<FavoriteMusicInfo>, Source<List<IMusicInfo>>>() {
                    @Override
                    public Source<List<IMusicInfo>> apply(List<FavoriteMusicInfo> input) {
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
