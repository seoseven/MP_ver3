package com.nwu.ypf.mp_ver3.bean;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
/**
 * 写在SQLite里的我喜爱歌单数据，比IMusicInfo多了一个添加时间字段，根据添加时间来排序
 * */
@Entity(tableName = "recent_music")
public class RecentMusicInfo implements IMusicInfo {

    @PrimaryKey
    private long id = -1;

    /**
     * 最后播放时间，使用 {@link System#currentTimeMillis()} 赋值。
     */
    @ColumnInfo(name = "last_play_time")
    private long lastPlayTime = 0;

    @ColumnInfo(name = "album_id")
    private int albumId = -1;

    @ColumnInfo(name = "album_name")
    @Nullable
    private String albumName;

    private int duration;

    @ColumnInfo(name = "music_name")
    @NonNull
    private String musicName;

    @Nullable
    private String artist;

    @ColumnInfo(name = "artist_id")
    private long artistId;

    private int size;

    @Nullable
    private String file;

    @NonNull
    private Uri uri;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecentMusicInfo)) return false;
        RecentMusicInfo musicInfo = (RecentMusicInfo) o;
        return getId() == musicInfo.getId() &&
                getAlbumId() == musicInfo.getAlbumId() &&
                getDuration() == musicInfo.getDuration() &&
                getArtistId() == musicInfo.getArtistId() &&
                getLastPlayTime() == musicInfo.getLastPlayTime() &&
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
                getMusicName(), getArtist(), getArtistId(), getSize(), getFile(), getUri(),
                getLastPlayTime());
    }

    public RecentMusicInfo(@NonNull String musicName, @NonNull Uri uri) {
        this.musicName = musicName;
        this.uri = uri;
    }

    public RecentMusicInfo(IMusicInfo musicInfo) {
        this.id = musicInfo.getId();
        this.albumId = musicInfo.getAlbumId();
        this.albumName = musicInfo.getAlbumName();
        this.duration = musicInfo.getDuration();
        this.musicName = musicInfo.getMusicName();
        this.artist = musicInfo.getArtist();
        this.artistId = musicInfo.getArtistId();
        this.size = musicInfo.getSize();
        this.file = musicInfo.getFile();
        this.uri = musicInfo.getUri();

        this.lastPlayTime = System.currentTimeMillis();
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

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
}