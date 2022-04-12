package com.nwu.ypf.mp_ver3.util.strategy;

import androidx.annotation.Nullable;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

import java.util.List;

public class CircleStrategy extends BaseStrategy {

    @Nullable
    @Override
    public IMusicInfo previous(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                               boolean fromUser) {
        return circlePrevious(current, list);
    }

    @Nullable
    @Override
    public IMusicInfo next(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                           boolean fromUser) {
        return circleNext(current, list);
    }
}
