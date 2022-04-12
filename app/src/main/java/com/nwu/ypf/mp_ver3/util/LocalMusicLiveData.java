package com.nwu.ypf.mp_ver3.util;

import android.content.Context;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.repository.MusicRepo;

import java.util.List;

public class LocalMusicLiveData extends ContentProviderLiveData<List<IMusicInfo>> {

    private final MusicRepo repo;

    public LocalMusicLiveData(Context context, MusicRepo repo) {
        super(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        this.repo = repo;
    }

    @Override
    void getContentProviderValue(final Callback<List<IMusicInfo>> callback) {
        repo.scanLocalMusic(context, 0L, 1L, null,
                new MusicRepo.ScanLocalMusicCallback() {
                    @Override
                    public void onFinish(@Nullable List<IMusicInfo> music) {
                        callback.onLoaded(music);
                    }
                });
    }
}
