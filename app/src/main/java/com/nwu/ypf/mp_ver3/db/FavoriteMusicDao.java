package com.nwu.ypf.mp_ver3.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nwu.ypf.mp_ver3.bean.FavoriteMusicInfo;

import java.util.List;

/**
 * 安卓Room框架，SQL语言都给你写好了，十分简洁好用
 */
@Dao
public abstract class FavoriteMusicDao {

    @Query("SELECT * FROM favorite_music ORDER BY add_time DESC")
    public abstract LiveData<List<FavoriteMusicInfo>> fetchFavoriteMusic();

    @Query("SELECT * FROM favorite_music WHERE id == :musicId")
    public abstract LiveData<FavoriteMusicInfo> queryFavoriteMusic(long musicId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract long insert(FavoriteMusicInfo favoriteMusicInfo);

    @Query("DELETE FROM favorite_music WHERE id == :musicId")
    public abstract void delete(long musicId);

    @Update
    public abstract void update(FavoriteMusicInfo favoriteMusicInfo);

    public void upsert(FavoriteMusicInfo musicInfo) {
        long r = insert(musicInfo);
        if (r == -1L) {
            update(musicInfo);
        }
    }
}
