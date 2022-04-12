package com.nwu.ypf.mp_ver3.util.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

import java.io.InputStream;

public class MusicLegacyModelLoaderFactory implements ModelLoaderFactory<IMusicInfo, InputStream> {

    private Context context;

    MusicLegacyModelLoaderFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public ModelLoader<IMusicInfo, InputStream> build(@NonNull MultiModelLoaderFactory
                                                             multiFactory) {
        return new MusicLegacyModelLoader(context);
    }

    @Override
    public void teardown() {
    }
}
