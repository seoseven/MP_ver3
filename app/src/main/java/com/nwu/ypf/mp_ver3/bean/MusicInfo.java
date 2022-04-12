package com.nwu.ypf.mp_ver3.bean;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * 除了歌单里的歌其他都用这个数据类型
 */
public class MusicInfo implements IMusicInfo {

    private long id = -1;

    private int albumId = -1;

    @Nullable
    private String albumName;

    private int duration;

    @NonNull
    private String musicName;

    @Nullable
    private String artist;

    private long artistId;

    private int size;

    @Nullable
    private String file;

    @NonNull
    private Uri uri;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicInfo)) return false;
        MusicInfo musicInfo = (MusicInfo) o;
        return getId() == musicInfo.getId() &&
                getAlbumId() == musicInfo.getAlbumId() &&
                getDuration() == musicInfo.getDuration() &&
                getArtistId() == musicInfo.getArtistId() &&
                getSize() == musicInfo.getSize() &&
                Objects.equals(getUri(), musicInfo.getUri()) &&
                Objects.equals(getAlbumName(), musicInfo.getAlbumName()) &&
                getMusicName().equals(musicInfo.getMusicName()) &&
                Objects.equals(getArtist(), musicInfo.getArtist()) &&
                Objects.equals(getFile(), musicInfo.getFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAlbumId(), getAlbumName(), getDuration(),
                getMusicName(), getArtist(), getArtistId(), getSize(), getFile(), getUri());
    }

    public MusicInfo(@NonNull String musicName, @NonNull Uri uri) {
        this.musicName = musicName;
        this.uri = uri;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getAlbumId() {
        return albumId;
    }

    @Override
    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    @Override
    @Nullable
    public String getAlbumName() {
        return albumName;
    }

    @Override
    public void setAlbumName(@Nullable String albumName) {
        this.albumName = albumName;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    @NonNull
    public String getMusicName() {
        return musicName;
    }

    @Override
    public void setMusicName(@NonNull String musicName) {
        this.musicName = musicName;
    }

    @Override
    @Nullable
    public String getArtist() {
        return artist;
    }

    @Override
    public void setArtist(@Nullable String artist) {
        this.artist = artist;
    }

    @Override
    public long getArtistId() {
        return artistId;
    }

    @Override
    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    @Nullable
    public String getFile() {
        return file;
    }

    @Override
    public void setFile(@Nullable String file) {
        this.file = file;
    }

    @Override
    @NonNull
    public Uri getUri() {
        return uri;
    }

}