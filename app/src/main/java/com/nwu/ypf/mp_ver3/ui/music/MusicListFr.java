package com.nwu.ypf.mp_ver3.ui.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.databinding.FrMusicBinding;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListFr;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;
import com.nwu.ypf.mp_ver3.ui.main.SharedViewModel;

import java.util.List;

public class MusicListFr extends BaseMusicListFr {

    private FrMusicBinding binding;
    private SharedViewModel sharedModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    protected BaseMusicListViewModel getViewModel() {
        return sharedModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FrMusicBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedModel.localMusicList.observe(getViewLifecycleOwner(),
                new Observer<Source<List<IMusicInfo>>>() {
                    @Override
                    public void onChanged(Source<List<IMusicInfo>> listSource) {
                        binding.setSource(listSource);
                    }
                });
    }

    @Override
    protected RecyclerView getMusicRecyclerView() {
        return binding.musicList;
    }

}
