package com.nwu.ypf.mp_ver3.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.nwu.ypf.mp_ver3.PlayerService;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.databinding.FrSearchResultBinding;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListFr;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;
import com.nwu.ypf.mp_ver3.util.PlayManager;

import java.util.List;

public class SearchResultFr extends BaseMusicListFr {

    private FrSearchResultBinding binding;
    private SharedViewModel sharedModel;
    private SearchResultViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SearchResultViewModel.class);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FrSearchResultBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void performSearch(@Nullable String text) {
        model.performSearch(text);
    }

    @Override
    protected BaseMusicListViewModel getViewModel() {
        return model;
    }

    @Override
    protected RecyclerView getMusicRecyclerView() {
        return binding.musicList;
    }

    @Override
    public void onItemClick(int position, IMusicInfo data) {
        startPlayMusic(data);
    }

    private void startPlayMusic(@NonNull IMusicInfo music) {
        List<IMusicInfo> list = sharedModel.localMusicList.getValue().getData();
        if (list != null) {
            PlayManager playManager = PlayManager.getInstance(requireContext());
            playManager.setPlayingList(list);
            playManager.setPlayingMusic(music, false);
            playManager.play();
            requireContext().startService(new Intent(requireContext(), PlayerService.class));
        }
    }
}
