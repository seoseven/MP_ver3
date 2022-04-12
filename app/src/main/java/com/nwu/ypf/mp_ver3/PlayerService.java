package com.nwu.ypf.mp_ver3;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.ui.main.MainActivity;
import com.nwu.ypf.mp_ver3.util.PlayManager;
import com.nwu.ypf.mp_ver3.util.PlayState;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {

    private static final int NOTIFY_ID = 1;
    private static final String NOTIFY_CHANNEL = "player";

    private static final int MSG_UPDATE = 1;

    private PlayManager playManager;
    private MediaPlayer mediaPlayer;

    /**
     * 用于保存 startId。
     * <p>
     * 有这样一种情况：用户未离开播放界面却通过通知栏关闭了音乐，这会调用 {@link #stopSelf()} 来关闭服务。
     * 但是因为 Activity 依然绑定所以服务不会立即销毁。
     * 接着用户继续播放音乐并退出界面。那么此时 Activity 解除绑定，服务被销毁播放中断。
     * <p>
     * 利用 <code>startId</code>，在每次播放时都会刷新，确保服务不会在上述情况下意外中断。
     *
     * @see <a href="https://developer.android.google.cn/guide/components/services#Stopping">Android 开发文档</a>。
     */
    private int startId;

    private PlayingMusicObserver playingMusicObserver;
    private PlayStateObserver playStateObserver;
    private PlayPositionReceiver playPositionReceiver;
    @Nullable
    private IMusicInfo playingMusic;

    private class PlayingMusicObserver implements Observer<IMusicInfo> {

        @Override
        public void onChanged(IMusicInfo IMusicInfo) {
            playingMusic = IMusicInfo;
            if (IMusicInfo != null) {
                prepareToPlay(IMusicInfo.getUri());
                if (playManager.playState.getValue() == PlayState.Playing) {
                    play();
                }
            } else {
                stop();
            }
        }
    }

    private class PlayStateObserver implements Observer<PlayState> {
        @Override
        public void onChanged(PlayState playState) {
            switch (playState) {
                case Playing:
                    play();
                    break;
                case Pause:
                    pauseMusic();
                    break;
                case Stop:
                    stop();
                    break;
            }
        }
    }

    private class PlayPositionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PlayManager.ACTION_SET_PLAY_POSITION.equals(intent.getAction())) {
                int newPosition = intent.getIntExtra(PlayManager.EXTRA_POSITION, -1);
                if (newPosition >= 0) {
                    mediaPlayer.seekTo(newPosition);
                }
            }
        }
    }

    /**
     * 用于刷新歌曲进度。
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE) {
                if (mediaPlayer.isPlaying()) {
                    playManager.updatePlayPosition(mediaPlayer.getCurrentPosition());
                    startUpdateProgressIfNeeded();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        playManager = PlayManager.getInstance(PlayerService.this);
        createNotifyChannel();

        playingMusicObserver = new PlayingMusicObserver();
        playStateObserver = new PlayStateObserver();
        playPositionReceiver = new PlayPositionReceiver();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        PlayManager.getInstance(this).playingMusic.observeForever(playingMusicObserver);
        PlayManager.getInstance(this).playState.observeForever(playStateObserver);

        IntentFilter filter = new IntentFilter(PlayManager.ACTION_SET_PLAY_POSITION);
        LocalBroadcastManager.getInstance(this).registerReceiver(playPositionReceiver, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (playingMusicObserver != null) {
            PlayManager.getInstance(this).playingMusic.removeObserver(playingMusicObserver);
        }
        if (playStateObserver != null) {
            PlayManager.getInstance(this).playState.removeObserver(playStateObserver);
        }
        if (playPositionReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(playPositionReceiver);
        }
        handler.removeCallbacksAndMessages(null);
        release();
        super.onDestroy();
    }

    private void startUpdateProgressIfNeeded() {
        if (mediaPlayer.isPlaying()) {
            PlayManager.getInstance(this).updatePlayPosition(mediaPlayer.getCurrentPosition());
            handler.sendEmptyMessageDelayed(MSG_UPDATE, 500);
        } else {
            handler.removeMessages(MSG_UPDATE);
        }
    }

    /**
     * 音乐播放完毕监听。
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        PlayManager.getInstance(this).skipNext(false);
    }

    private void prepareToPlay(Uri music) {
        startForeground();
        loadMediaPlayerFile(music);
    }

    private void loadMediaPlayerFile(Uri music) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, music);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        startUpdateProgressIfNeeded();
    }

    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void release() {
        if (mediaPlayer != null) {
            stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startForeground() {
        startForeground(NOTIFY_ID, buildNotify());
    }

    /**
     * 构建常驻通知栏的通知。
     */
    private Notification buildNotify() {
        // 通知被点击时的响应
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        String title = playingMusic != null ? playingMusic.getMusicName()
                : getString(R.string.notify_player_waiting_play);
        String msg = playingMusic != null ? playingMusic.getArtist() + " - " +
                playingMusic.getAlbumName() : "";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFY_CHANNEL)
                .setSmallIcon(R.drawable.ic_nav_music)
                .setContentTitle(title)
                .setContentText(msg)
                .setOngoing(true)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
    }

    /**
     * 注册通知渠道。
     * Android O 及以上系统中必须注册渠道才才能显示通知。
     */
    private void createNotifyChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notify_channel_player_name);
            String description = getString(R.string.notify_channel_player_desc);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(NOTIFY_CHANNEL, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
