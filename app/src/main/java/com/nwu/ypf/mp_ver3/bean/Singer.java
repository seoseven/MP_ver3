package com.nwu.ypf.mp_ver3.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 *
 * 歌手类
 * List<IMusicInfo> music属性是因为要用Hashmap进行一个分组操作
 */
public class Singer {

    private long id;
    @Nullable
    private String name;
    @NonNull
    private List<IMusicInfo> music;

    public Singer(@NonNull IMusicInfo music) {
        this.id = music.getArtistId();
        this.name = music.getArtist();
        this.music = new ArrayList<>();
        this.music.add(music);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Singer)) return false;
        Singer singer = (Singer) o;
        return getId() == singer.getId() &&
                Objects.equals(getName(), singer.getName()) &&
                getMusic().equals(singer.getMusic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMusic());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IMusicInfo> getMusic() {
        return music;
    }

    public void setMusic(List<IMusicInfo> music) {
        this.music = music;
    }
}
