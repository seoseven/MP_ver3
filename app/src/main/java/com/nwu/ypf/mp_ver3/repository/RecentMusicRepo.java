package com.nwu.ypf.mp_ver3.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.nwu.ypf.mp_ver3.bean.RecentMusicInfo;
import com.nwu.ypf.mp_ver3.db.MainDb;
import com.nwu.ypf.mp_ver3.db.RecentMusicDao;

import java.util.List;

public class RecentMusicRepo {

    private static RecentMusicRepo INSTANCE;

    public static RecentMusicRepo getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RecentMusicRepo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecentMusicRepo(context);
                }
            }
        }
        return INSTANCE;
    }

    private RecentMusicDao dao;

    private RecentMusicRepo(Context context) {
        dao = MainDb.getInstance(context).recentMusicDao();
    }

    public LiveData<List<RecentMusicInfo>> getRecentMusic() {
        return dao.fetchRecentMusic();
    }

    /**
     * 开且一个新线程来插入或更新最近播放歌曲。
     */
    public void upsert(final RecentMusicInfo recentMusicInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.upsert(recentMusicInfo);
            }
        }).start();
    }

}
