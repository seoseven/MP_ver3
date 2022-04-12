package com.nwu.ypf.mp_ver3.ui.albummusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.databinding.FrAlbumMusicBinding;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListFr;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;
import com.nwu.ypf.mp_ver3.ui.main.SharedViewModel;
import com.nwu.ypf.mp_ver3.util.PlayManager;

public class AlbumMusicFr extends BaseMusicListFr implements View.OnClickListener {

    private FrAlbumMusicBinding binding;
    private SharedViewModel sharedModel;
    private AlbumMusicViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model = new ViewModelProvider(this).get(AlbumMusicViewModel.class);
        model.setMusicList(sharedModel.getCurrentAlbum());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FrAlbumMusicBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        PlayManager playManager = PlayManager.getInstance(requireContext());
        binding.setPlayManager(playManager);
        binding.setClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setTitle(sharedModel.getCurrentAlbum().getName());
        setupActionBar(binding.toolbar, true);
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
    public void onClick(View v) {
        if (v.getId() == binding.miniPlayer.getRoot().getId()) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.playerFr);
        }
    }
}
