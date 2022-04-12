package com.nwu.ypf.mp_ver3.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * 专辑类
 * List<IMusicInfo> music属性是因为要用Hashmap进行一个分组操作
 */

public class Album {

    private final int id;
    @Nullable
    private String name;
    @NonNull
    private List<IMusicInfo> music;

    public Album(@NonNull IMusicInfo musicInfo) {
        this.id = musicInfo.getAlbumId();
        this.name = musicInfo.getAlbumName();
        this.music = new ArrayList<>();
        this.music.add(musicInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album)) return false;
        Album album = (Album) o;
        return getId() == album.getId() &&
                Objects.equals(getName(), album.getName()) &&
                getMusic().equals(album.getMusic());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMusic());
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @NonNull
    public List<IMusicInfo> getMusic() {
        return music;
    }

    public void setMusic(@NonNull List<IMusicInfo> music) {
        this.music = music;
    }
}
