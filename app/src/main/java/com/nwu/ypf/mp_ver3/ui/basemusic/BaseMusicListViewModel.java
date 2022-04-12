package com.nwu.ypf.mp_ver3.ui.basemusic;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;

import java.util.List;

public interface BaseMusicListViewModel {

    /**
     * 获取当前页面所要显示的歌曲列表。此函数在 {@link BaseMusicListFr#onViewCreated(View, Bundle)} 中被调用。
     */
    LiveData<Source<List<IMusicInfo>>> getMusicList();
}
