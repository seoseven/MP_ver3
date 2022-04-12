package com.nwu.ypf.mp_ver3.ui.basemusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nwu.ypf.mp_ver3.PlayerService;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.ui.common.BaseFr;
import com.nwu.ypf.mp_ver3.ui.common.BaseListAdapter;
import com.nwu.ypf.mp_ver3.util.PlayManager;

import java.util.List;
import java.util.Objects;

/**
 * 用于显示歌曲列表，列表数据由 {@link BaseMusicListViewModel#getMusicList()} 提供。
 * 默认点击列表项会设置 {@link BaseMusicListViewModel#getMusicList()} 为播放列表，并开始播放点击的歌曲。若想
 * 改变此行为需要覆盖 {@link #onItemClick(int, IMusicInfo)} 函数。
 */
public abstract class BaseMusicListFr extends BaseFr implements
        BaseListAdapter.OnItemClickListener<IMusicInfo> {

    private BaseMusicListViewModel model;

    private MusicListAdapter adapter;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract BaseMusicListViewModel getViewModel();

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.model = getViewModel();
        RecyclerView musicList = getMusicRecyclerView();
        adapter = new MusicListAdapter(this, getViewLifecycleOwner());
        musicList.setLayoutManager(new LinearLayoutManager(requireContext()));
        musicList.setAdapter(adapter);

        model.getMusicList().observe(getViewLifecycleOwner(),
                new Observer<Source<List<IMusicInfo>>>() {
                    @Override
                    public void onChanged(Source<List<IMusicInfo>> listSource) {
                        adapter.submitList(listSource.getData());
                    }
                });
    }

    protected abstract RecyclerView getMusicRecyclerView();

    @Override
    public void onItemClick(int position, IMusicInfo data) {
        startPlayMusic(data);
    }

    private void startPlayMusic(@NonNull IMusicInfo music) {
        List<IMusicInfo> list = Objects.requireNonNull(model.getMusicList().getValue()).getData();
        if (list != null) {
            PlayManager playManager = PlayManager.getInstance(requireContext());
            playManager.setPlayingList(list);
            playManager.setPlayingMusic(music, false);
            playManager.play();
            requireContext().startService(new Intent(requireContext(), PlayerService.class));
        }
    }
}

