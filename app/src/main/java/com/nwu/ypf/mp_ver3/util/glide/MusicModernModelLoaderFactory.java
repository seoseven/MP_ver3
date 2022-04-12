package com.nwu.ypf.mp_ver3.util.glide;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

public class MusicModernModelLoaderFactory implements ModelLoaderFactory<IMusicInfo, Bitmap> {

    private Context context;

    MusicModernModelLoaderFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public ModelLoader<IMusicInfo, Bitmap> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new MusicModernModelLoader(context);
    }

    @Override
    public void teardown() {
    }
}
