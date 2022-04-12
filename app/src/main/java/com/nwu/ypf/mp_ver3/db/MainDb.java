package com.nwu.ypf.mp_ver3.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.nwu.ypf.mp_ver3.bean.FavoriteMusicInfo;
import com.nwu.ypf.mp_ver3.bean.RecentMusicInfo;

/**
 * Room框架下的建表操作
 */
@Database(entities = {RecentMusicInfo.class, FavoriteMusicInfo.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MainDb extends RoomDatabase {

    private static volatile MainDb INSTANCE;

    public static MainDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MainDb.class) {
                if (INSTANCE == null) {
                    // 实例化
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MainDb.class, "main-db").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract RecentMusicDao recentMusicDao();

    public abstract FavoriteMusicDao favoriteMusicDao();
}
