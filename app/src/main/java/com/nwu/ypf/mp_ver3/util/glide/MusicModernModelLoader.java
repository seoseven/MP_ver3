package com.nwu.ypf.mp_ver3.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.util.Util;

import java.io.IOException;
/**
 * Glide框架下对于安卓9及以下版本的读取图片的方法
 */
public class MusicModernModelLoader implements ModelLoader<IMusicInfo, Bitmap> {

    private Context context;

    MusicModernModelLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public LoadData<Bitmap> buildLoadData(@NonNull IMusicInfo IMusicInfo, int width, int height,
                                          @NonNull Options options) {
        Key diskCacheKey = new ObjectKey(IMusicInfo);
        return new LoadData<>(diskCacheKey, new MusicModernDataFetcher(context, IMusicInfo, width,
                height));
    }

    @Override
    public boolean handles(@NonNull IMusicInfo IMusicInfo) {
        return Util.shouldUseModernThumbApi();
    }

    public static class MusicModernDataFetcher implements DataFetcher<Bitmap> {

        private Context context;
        private IMusicInfo IMusicInfo;
        private int width, height;

        MusicModernDataFetcher(Context context, IMusicInfo IMusicInfo, int width, int height) {
            this.context = context.getApplicationContext();
            this.IMusicInfo = IMusicInfo;
            this.width = width;
            this.height = height;
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void loadData(@NonNull Priority priority,
                             @NonNull DataCallback<? super Bitmap> callback) {
            if (IMusicInfo == null) {
                callback.onLoadFailed(new IllegalArgumentException("IMusicInfo is null"));
                return;
            }
            Size size = new Size(width, height);
            try {
                Bitmap b = context.getContentResolver().loadThumbnail(IMusicInfo.getUri(), size,
                        null);
                callback.onDataReady(b);
            } catch (IOException e) {
                callback.onLoadFailed(e);
            }
        }

        @Override
        public void cleanup() {
        }

        @Override
        public void cancel() {
        }

        @NonNull
        @Override
        public Class<Bitmap> getDataClass() {
            return Bitmap.class;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }
}
