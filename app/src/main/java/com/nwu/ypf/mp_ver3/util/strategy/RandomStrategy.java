package com.nwu.ypf.mp_ver3.util.strategy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

import java.util.List;
import java.util.Random;
import java.util.Stack;

public class RandomStrategy extends BaseStrategy {

    private final Random random;
    private final Stack<IMusicInfo> backStack;

    public RandomStrategy() {
        random = new Random();
        backStack = new Stack<>();
    }

    @Override
    public void onPlay(@NonNull IMusicInfo info) {
        super.onPlay(info);
        if (!backStack.empty()) {
            if (!backStack.peek().equals(info)) {
                backStack.push(info);
            }
        } else {
            backStack.push(info);
        }
    }

    @Nullable
    private IMusicInfo getRandom(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }

        int i = Math.abs(random.nextInt()) % list.size();
        while (list.get(i).equals(current)) {
            i = Math.abs(random.nextInt()) % list.size();
        }
        return list.get(i);
    }

    @Nullable
    @Override
    public IMusicInfo previous(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                               boolean fromUser) {
        IMusicInfo target;
        if (backStack.empty()) {
            target = getRandom(current, list);
        } else {
            backStack.pop(); // 弹出自身
            if (backStack.empty()) {
                target = getRandom(current, list);
            } else {
                target = backStack.pop();
            }
        }
        return target;
    }

    @Nullable
    @Override
    public IMusicInfo next(@Nullable IMusicInfo current, @Nullable List<IMusicInfo> list,
                           boolean fromUser) {
        return getRandom(current, list);
    }
}
