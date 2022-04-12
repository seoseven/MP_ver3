package com.nwu.ypf.mp_ver3.util;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.bean.Album;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.util.glide.GlideApp;

/**
 * 在布局文件中可以使用的新属性
 */

public class BindingAdapters {

    @BindingAdapter("visibleGone")
    public static void visibleGone(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("musicAlbumPic")
    public static void musicAlbumPic(ImageView imageView, @Nullable IMusicInfo musicInfo) {
        GlideApp.with(imageView.getContext())
                .load(musicInfo)
                .error(R.drawable.img_default_artist_album)
                .into(imageView);
    }

    @BindingAdapter("musicAlbumPic")
    public static void musicAlbumPic(ImageView imageView, @Nullable Album album) {
        IMusicInfo music = null;
        if (album != null && !album.getMusic().isEmpty()) {
            music = album.getMusic().get(0);
        }
        GlideApp.with(imageView.getContext())
                .load(music)
                .error(R.drawable.img_default_artist_album)
                .into(imageView);
    }
}
