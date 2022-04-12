package com.nwu.ypf.mp_ver3.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.util.strategy.CircleStrategy;
import com.nwu.ypf.mp_ver3.util.strategy.LinearStrategy;
import com.nwu.ypf.mp_ver3.util.strategy.MusicSwitchStrategy;
import com.nwu.ypf.mp_ver3.util.strategy.RandomStrategy;
import com.nwu.ypf.mp_ver3.util.strategy.SingleStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Util {

    private static final String TAG = "Util";
//返回播放停止图标
    @DrawableRes
    public static int getPlayerPlayButtonRes(PlayState state) {
        if (state == null) {
            return R.drawable.ic_player_play;
        }
        switch (state) {
            case Playing:
                return R.drawable.ic_player_pause;
            case Pause:
            case Stop:
                return R.drawable.ic_player_play;
        }
        return R.drawable.ic_player_play;
    }
//返回播放模式图标
    @DrawableRes
    public static int getMusicSwitchStrategyIconRes(MusicSwitchStrategy strategy) {
        if (strategy instanceof LinearStrategy) {
            return R.drawable.ic_player_strategy_linear;
        } else if (strategy instanceof CircleStrategy) {
            return R.drawable.ic_player_strategy_circle;
        } else if (strategy instanceof RandomStrategy) {
            return R.drawable.ic_player_strategy_random;
        } else if (strategy instanceof SingleStrategy) {
            return R.drawable.ic_player_strategy_single;
        } else {
            Log.e(TAG, "Unknown strategy type: " + strategy.getClass().getSimpleName());
            return R.drawable.ic_player_strategy_single;
        }
    }

    /**
     * 获取音乐标题颜色。
     *
     * @param current 正在播放的音乐。
     * @param target  要显示的音乐。
     * @return 应当使用的颜色。
     */
    @ColorInt
    public static int getMusicNameColor(Context context, IMusicInfo current, IMusicInfo target) {
        TypedValue typedValue = new TypedValue();
        if (current == null || target == null || current.getId() != target.getId()) {
            TypedArray ta = context.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorPrimary});
            int color = ta.getColor(0, Color.BLACK);
            ta.recycle();
            return color;
        } else {
            context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
            return typedValue.data;
        }
    }

    /**
     * 获取音乐定时器的颜色。
     *
     * @param scheduleTime 停止播放的时间，基于 {@link SystemClock#uptimeMillis()}.
     * @return 应当使用的颜色。
     */
    @ColorInt
    public static int getTimerColor(Context context, long scheduleTime) {
        TypedValue typedValue = new TypedValue();
        if (scheduleTime == 0 || scheduleTime < SystemClock.uptimeMillis()) {
            TypedArray ta = context.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorPrimary});
            int color = ta.getColor(0, Color.BLACK);
            ta.recycle();
            return color;
        } else {
            context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
            return typedValue.data;
        }
    }
//歌曲进度时间格式转换
    public static String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return simpleDateFormat.format(date);
    }
//判断手机安卓版本从而调用Glide中的不同获取图片方法
    public static boolean shouldUseModernThumbApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
//歌曲大小格式转换
    public static String parseFileSize(long size) {
        if (size <= 1000) {
            return size + "Byte";
        } else if (size <= 1000 * 1000) {
            return String.format(Locale.getDefault(), "%.2f KB", size / 1024f);
        } else {
            return String.format(Locale.getDefault(), "%.2f MB", size / (1024 * 1024f));
        }
    }
}
