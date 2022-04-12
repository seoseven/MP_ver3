package com.nwu.ypf.mp_ver3.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nwu.ypf.mp_ver3.bean.RecentMusicInfo;

import java.util.List;

/**
 * SQL模板
 */
@Dao
public abstract class RecentMusicDao {

    @Query("SELECT * FROM recent_music ORDER BY last_play_time DESC")
    public abstract LiveData<List<RecentMusicInfo>> fetchRecentMusic();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract long insert(RecentMusicInfo recentMusicInfo);

    @Update
    public abstract void update(RecentMusicInfo recentMusicInfo);

    public void upsert(RecentMusicInfo musicInfo) {
        long r = insert(musicInfo);
        if (r == -1L) {
            update(musicInfo);
        }
    }
}
