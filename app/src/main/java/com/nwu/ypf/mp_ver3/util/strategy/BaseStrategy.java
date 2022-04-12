package com.nwu.ypf.mp_ver3.util.strategy;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.ui.player.PlayerFr;

import java.util.List;

public abstract class BaseStrategy implements MusicSwitchStrategy {

    private static final String TAG = "BaseStrategy";

    @Override
    public void onPlay(@NonNull IMusicInfo info) {
    }

    /**
     * 寻找给定歌曲在播放列表中的下标。不存在返回 -1.
     */
    protected int findMusicIndex(@Nullable IMusicInfo music, @Nullable List<IMusicInfo> list) {
        if (music == null) {
            return -1;
        }
        if (list == null || list.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(music)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取当前播放列表中的第一首歌。
     */
    @Nullable
    protected IMusicInfo getFirstMusic(@Nullable List<IMusicInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取播放列表中上一首歌。若已经是第一首或不存在返回 null.
     */
    protected IMusicInfo getPreviousMusic(int currentIndex, @Nullable List<IMusicInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (currentIndex == 0) {
            // 已经是第一首
            return null;
        }
        return list.get(currentIndex - 1);
    }

    /**
     * 获取播放列表中下一首歌。若已经是最后一首或不存在返回 null.
     */
    @Nullable
    protected IMusicInfo getNextMusic(int currentIndex, @Nullable List<IMusicInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (currentIndex == list.size() - 1) {
            // 已经是最后一首
            return null;
        }
        return list.get(currentIndex + 1);
    }

    protected IMusicInfo circlePrevious(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list) {
        IMusicInfo target;
        int currentIndex = findMusicIndex(current, list);
        if (currentIndex >= 0) {
            target = getPreviousMusic(currentIndex, list);
            if (target == null && list != null && !list.isEmpty()) {
                // 当前播放的已经是第一首
                target = list.get(list.size() - 1);
            }
        } else {
            target = getFirstMusic(list);
        }
        return target;
    }

    protected IMusicInfo circleNext(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list) {
        IMusicInfo target;
        int currentIndex = findMusicIndex(current, list);
        if (currentIndex >= 0) {
            target = getNextMusic(currentIndex, list);
            if (target == null && list != null && !list.isEmpty()) {
                // 当前播放的已经是最后一首
                target = list.get(0);
            }
        } else {
            target = getFirstMusic(list);
        }
        return target;
    }

    @NonNull
    @Override
    public MusicSwitchStrategy toggle() {
        MusicSwitchStrategy current = this;
        if (current instanceof LinearStrategy) {

            return new CircleStrategy();
        } else if (current instanceof CircleStrategy) {
            return new RandomStrategy();
        } else if (current instanceof RandomStrategy) {
            return new SingleStrategy();
        } else if (current instanceof SingleStrategy) {
            return new LinearStrategy();
        } else {
            Log.e(TAG, "Unknown strategy type: " + current.getClass().getSimpleName());
            return new LinearStrategy();
        }
    }
}
