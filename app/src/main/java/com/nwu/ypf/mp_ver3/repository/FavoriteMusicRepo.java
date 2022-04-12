package com.nwu.ypf.mp_ver3.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.nwu.ypf.mp_ver3.bean.FavoriteMusicInfo;
import com.nwu.ypf.mp_ver3.db.FavoriteMusicDao;
import com.nwu.ypf.mp_ver3.db.MainDb;

import java.util.List;

public class FavoriteMusicRepo {

    private static FavoriteMusicRepo INSTANCE;

    public static FavoriteMusicRepo getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteMusicRepo.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteMusicRepo(context);
                }
            }
        }
        return INSTANCE;
    }

    private FavoriteMusicDao dao;

    private FavoriteMusicRepo(Context context) {
        dao = MainDb.getInstance(context).favoriteMusicDao();
    }

    public LiveData<List<FavoriteMusicInfo>> getFavoriteMusic() {
        return dao.fetchFavoriteMusic();
    }

    public LiveData<FavoriteMusicInfo> queryFavoriteMusic(long musicId) {
        return dao.queryFavoriteMusic(musicId);
    }

    /**
     * 开且一个新线程来删除。
     */
    public void removeFavorite(final long musicId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.delete(musicId);
            }
        }).start();
    }

    /**
     * 开且一个新线程来插入或更新。
     */
    public void upsert(final FavoriteMusicInfo favoriteMusicInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.upsert(favoriteMusicInfo);
            }
        }).start();
    }

}
