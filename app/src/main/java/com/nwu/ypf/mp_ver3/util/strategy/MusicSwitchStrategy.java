package com.nwu.ypf.mp_ver3.util.strategy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

import java.util.List;

/**
 * 因为四种播放模式都是和上/下一首直接联系的，所以我们把上下一首方法抽象出来
 */
public interface MusicSwitchStrategy {

    /**
     * 每当一个歌曲被播放时就调用一次。
     */
    void onPlay(@NonNull IMusicInfo info);

    /**
     * 切换到上一首歌曲。
     *
     * @param current  当前播放的歌曲。
     * @param list     播放列表。
     * @param fromUser 是否由用户触发切换。
     * @return 要切换到的目标歌曲，没有返回 <code>null</code>.
     */
    @Nullable
    IMusicInfo previous(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                        boolean fromUser);

    /**
     * 切换到下一首歌曲。
     *
     * @param current  当前播放的歌曲。
     * @param list     播放列表。
     * @param fromUser 是否由用户触发切换。
     * @return 要切换到的目标歌曲，没有返回 <code>null</code>.
     */
    @Nullable
    IMusicInfo next(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list, boolean fromUser);

    @NonNull
    MusicSwitchStrategy toggle();

}
