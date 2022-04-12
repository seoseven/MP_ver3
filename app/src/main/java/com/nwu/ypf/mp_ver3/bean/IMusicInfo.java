package com.nwu.ypf.mp_ver3.bean;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 方便不同歌曲类型复用
 */
public interface IMusicInfo {
    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    long getId();

    void setId(long id);

    int getAlbumId();

    void setAlbumId(int albumId);

    @Nullable
    String getAlbumName();

    void setAlbumName(@Nullable String albumName);

    int getDuration();

    void setDuration(int duration);

    @NonNull
    String getMusicName();

    void setMusicName(@NonNull String musicName);

    @Nullable
    String getArtist();

    void setArtist(@Nullable String artist);

    long getArtistId();

    void setArtistId(long artistId);

    int getSize();

    void setSize(int size);

    @Nullable
    String getFile();

    void setFile(@Nullable String file);

    @NonNull
    Uri getUri();
}
