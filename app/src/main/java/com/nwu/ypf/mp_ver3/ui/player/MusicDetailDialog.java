package com.nwu.ypf.mp_ver3.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.databinding.DialogMusicDetailBinding;

public class MusicDetailDialog extends BottomSheetDialogFragment {

    private DialogMusicDetailBinding binding;

    private LiveData<IMusicInfo> musicInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogMusicDetailBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setMusic(musicInfo);
        return binding.getRoot();
    }

    public MusicDetailDialog setMusicInfo(LiveData<IMusicInfo> musicInfo) {
        this.musicInfo = musicInfo;
        if (binding != null) {
            binding.setMusic(musicInfo);
        }
        return this;
    }

}
