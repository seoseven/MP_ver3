package com.nwu.ypf.mp_ver3.util.glide;

import android.content.ContentResolver;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.repository.MusicRepo;
import com.nwu.ypf.mp_ver3.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Glide框架下对于安卓10版本的读取图片的方法
 */
public class MusicLegacyModelLoader implements ModelLoader<IMusicInfo, InputStream> {

    private Context context;

    MusicLegacyModelLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull IMusicInfo IMusicInfo, int width, int height,
                                               @NonNull Options options) {
        Key diskCacheKey = new ObjectKey(IMusicInfo);
        return new LoadData<>(diskCacheKey, new LegacyFetcher(context, IMusicInfo));
    }

    @Override
    public boolean handles(@NonNull IMusicInfo IMusicInfo) {
        return !Util.shouldUseModernThumbApi();
    }

    public static class LegacyFetcher implements DataFetcher<InputStream> {

        @Nullable
        private IMusicInfo IMusicInfo;
        @NonNull
        private ContentResolver cr;

        LegacyFetcher(Context context, @Nullable IMusicInfo info) {
            this.IMusicInfo = info;
            this.cr = context.getContentResolver();
        }

        @Override
        public void loadData(@NonNull Priority priority,
                             @NonNull DataCallback<? super InputStream> callback) {
            if (IMusicInfo == null) {
                callback.onLoadFailed(new IllegalArgumentException("IMusicInfo is null"));
                return;
            }
            String thumb = MusicRepo.getAlbumArt(cr, IMusicInfo.getAlbumId());
            File file = null;
            if (thumb != null) file = new File(thumb);
            if (file == null || !file.isFile()) {
                callback.onLoadFailed(new IllegalArgumentException("Can not find thumb art."));
                return;
            }
            try {
                callback.onDataReady(new FileInputStream(file));
            } catch (FileNotFoundException e) {
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
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }
}
