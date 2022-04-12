package com.nwu.ypf.mp_ver3.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;

import java.io.InputStream;

/**
 * 判断系统版本后通过这个类选择不同的读取图片方法
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setLogLevel(Log.VERBOSE);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.prepend(IMusicInfo.class, Bitmap.class, new MusicModernModelLoaderFactory(context));
        registry.prepend(IMusicInfo.class, InputStream.class,
                new MusicLegacyModelLoaderFactory(context));
    }
}
