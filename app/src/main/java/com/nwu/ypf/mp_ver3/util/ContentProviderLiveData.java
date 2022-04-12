package com.nwu.ypf.mp_ver3.util;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

/**
 * 给从系统媒体数据库读取出来的音乐信息进行LiveData封装
 * @param <T>
 */

public abstract class ContentProviderLiveData<T> extends MutableLiveData<T> {

    protected final Context context;
    private final Uri uri;
    private final MyObserver observer;

    /**
     * @param uri 要监听的 uri.
     */
    public ContentProviderLiveData(Context context, Uri uri) {
        observer = new MyObserver(null);
        this.context = context.getApplicationContext();
        this.uri = uri;
    }

    private class MyObserver extends ContentObserver {
        MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            refreshData();
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        refreshData();
        context.getContentResolver().registerContentObserver(uri, true, observer);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        context.getContentResolver().unregisterContentObserver(observer);
    }

    private void refreshData() {
        Callback<T> callback = new Callback<T>() {
            @Override
            public void onLoaded(T data) {
                postValue(data);
            }
        };
        getContentProviderValue(callback);
    }

    abstract void getContentProviderValue(Callback<T> callback);

    public interface Callback<T> {
        void onLoaded(T data);
    }
}
