package com.nwu.ypf.mp_ver3.util.strategy;

import androidx.annotation.Nullable;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

import java.util.List;

public class LinearStrategy extends BaseStrategy {

    @Nullable
    @Override
    public IMusicInfo previous(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                               boolean fromUser) {
        if (fromUser) {
            return circlePrevious(current, list);
        }

        IMusicInfo target;
        int currentIndex = findMusicIndex(current, list);
        if (currentIndex >= 0) {
            target = getPreviousMusic(currentIndex, list);
        } else {
            target = getFirstMusic(list);
        }
        return target;
    }

    @Nullable
    @Override
    public IMusicInfo next(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                           boolean fromUser) {
        if (fromUser) {
            return circleNext(current, list);
        }

        IMusicInfo target;
        int currentIndex = findMusicIndex(current, list);
        if (currentIndex >= 0) {
            target = getNextMusic(currentIndex, list);
        } else {
            target = getFirstMusic(list);
        }
        return target;
    }
}
