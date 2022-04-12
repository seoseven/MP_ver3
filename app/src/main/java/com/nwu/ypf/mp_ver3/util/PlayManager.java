package com.nwu.ypf.mp_ver3.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.RecentMusicInfo;
import com.nwu.ypf.mp_ver3.repository.RecentMusicRepo;
import com.nwu.ypf.mp_ver3.util.strategy.LinearStrategy;
import com.nwu.ypf.mp_ver3.util.strategy.MusicSwitchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PlayManager类保存着各种数据的状态（歌曲播放状态，歌单状态，歌曲播放模式状态，单曲状态），通过对状态改变的监听实现对Service中各种方法的控制，
 */
public class PlayManager {
    private static final String TAG = "PlayManager";

    private static final int MSG_STOP = 1;
//这个volatile关键词过于复杂，和生命周期有关，保持和Service组件周期保持一致，才能实现对Service的控制
    private static volatile PlayManager INSTANCE;

    public static final String ACTION_SET_PLAY_POSITION = "SET_PLAY_POSITION";
    /**
     * 用于储存要跳转到的位置。
     */
    public static final String EXTRA_POSITION = "position";
//这里传context是因为下面有个方法需要这个参数
    @NonNull
    public static PlayManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PlayManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PlayManager(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 用于更新最近播放列表。（全部LiveData封装一层）
     */
    private RecentMusicRepo recentMusicRepo;

    private MutableLiveData<List<IMusicInfo>> _playingList;
    private MutableLiveData<IMusicInfo> _playingMusic;
    public LiveData<IMusicInfo> playingMusic;
    private MutableLiveData<PlayState> _playState;
    public LiveData<PlayState> playState;

    private MutableLiveData<MusicSwitchStrategy> _strategy;
    public LiveData<MusicSwitchStrategy> strategy;

    /**
     * 当前播放进度，由 {@link com.nwu.ypf.mp_ver3.PlayerService} 刷新，其他组件监听。
     */
    private MutableLiveData<Integer> _playPosition;
    public LiveData<Integer> playPosition;

    /**
     * 自动停止播放的时间，以 {@link SystemClock#uptimeMillis()} 为基准。<code>0</code> 表示不停止播放。
     */
    private MutableLiveData<Long> _autoStopTime = new MutableLiveData<>(0L);
    public LiveData<Long> autoStopTime = _autoStopTime;

    /**
     * 用于定时关闭。
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_STOP) {
                stop();
                removeMessages(MSG_STOP);
                _autoStopTime.postValue(0L);
            }
        }
    };
    //同理这里传context是因为下面有个方法需要这个参数
    private PlayManager(Context context) {
        recentMusicRepo = RecentMusicRepo.getInstance(context.getApplicationContext());

        _strategy = new MutableLiveData<>((MusicSwitchStrategy) new LinearStrategy());
        strategy = _strategy;

        _playingList = new MutableLiveData<>((List<IMusicInfo>) new ArrayList<IMusicInfo>());
        _playingMusic = new MutableLiveData<>(null);
        playingMusic = _playingMusic;
        _playState = new MutableLiveData<>(PlayState.Stop);
        playState = _playState;
        _playPosition = new MutableLiveData<>(0);
        playPosition = _playPosition;
    }

    public void setPlayingList(@NonNull List<IMusicInfo> list) {
        if (!list.equals(_playingList.getValue())) {
            _playingList.postValue(list);
        }
    }

    public void setPlayingMusic(@Nullable IMusicInfo music, boolean restart) {
        IMusicInfo playing = playingMusic.getValue();
        if (music == null || playing == null || music.getId() != playing.getId() || restart) {
            if (music != null) {
                recentMusicRepo.upsert(new RecentMusicInfo(music));
                getSwitchStrategy().onPlay(music);
            }
            _playingMusic.postValue(music);
        }
    }


    /**
     * 切换到上一首歌并开始播放。
     *
     * @param fromUser 是否由用户触发切换。
     */
    public void skipPrevious(boolean fromUser) {
        IMusicInfo target = getSwitchStrategy().previous(playingMusic.getValue(),
                _playingList.getValue(), fromUser);
        if (target != null) {
            setPlayingMusic(target, true);
            play();
        } else {
            stop();
        }
    }

    /**
     * 切换到下一首歌并开始播放。
     *
     * @param fromUser 是否由用户触发切换。
     */
    public void skipNext(boolean fromUser) {
        IMusicInfo target = getSwitchStrategy().next(playingMusic.getValue(),
                _playingList.getValue(), fromUser);
        if (target != null) {
            setPlayingMusic(target, true);
            play();
        } else {
            stop();
        }
    }

    @MainThread
    private void setState(PlayState state) {
        if (_playState.getValue() != state) {
            _playState.setValue(state);
        }
    }

    @MainThread
    public void play() {
        setState(PlayState.Playing);
    }

    @MainThread
    public void stop() {
        setState(PlayState.Stop);
    }


    /**
     * 在 {@link PlayState#Pause} 与 {@link PlayState#Playing} 之间切换状态。若当前状态为
     * {@link PlayState#Stop} 则直接切换至 {@link PlayState#Playing}。
     */
    @MainThread
    public void toggleState() {
        PlayState current = _playState.getValue();
        if (current == PlayState.Pause || current == PlayState.Stop) {
            setState(PlayState.Playing);
        } else {
            setState(PlayState.Pause);
            cancelAutoStop();
        }
    }

    /**
     * 更新播放进度。此函数只能由 {@link com.nwu.ypf.mp_ver3.PlayerService} 调用。
     *
     * @param position 最新的播放进度。
     */
    public void updatePlayPosition(int position) {
        _playPosition.postValue(position);
    }

    public void setPlayPosition(Context context, int newPosition) {
        Intent i = new Intent(ACTION_SET_PLAY_POSITION);
        i.putExtra(EXTRA_POSITION, newPosition);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }

    public void toggleSwitchStrategy() {
        _strategy.postValue(getSwitchStrategy().toggle());
    }

    @NonNull
    public MusicSwitchStrategy getSwitchStrategy() {
        return Objects.requireNonNull(strategy.getValue());
    }

    /**
     * @param targetTime 要停止播放的时间，以 {@link SystemClock#uptimeMillis()} 为基准。
     */
    public void setAutoStopTime(long targetTime) {
        if (targetTime < SystemClock.uptimeMillis() + 1000) {
            Log.e(TAG, "停止播放时间距离现在过近！忽略请求。");
        } else {
            handler.removeMessages(MSG_STOP);
            handler.sendEmptyMessageDelayed(MSG_STOP, targetTime - SystemClock.uptimeMillis());
            _autoStopTime.postValue(targetTime);
        }
    }

    public void cancelAutoStop() {
        handler.removeMessages(MSG_STOP);
        _autoStopTime.postValue(0L);
    }
}
