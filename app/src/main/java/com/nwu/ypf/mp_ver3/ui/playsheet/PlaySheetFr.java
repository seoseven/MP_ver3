package com.nwu.ypf.mp_ver3.ui.playsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.databinding.FrPlaysheetBinding;
import com.nwu.ypf.mp_ver3.ui.common.BaseFr;

public class PlaySheetFr extends BaseFr implements View.OnClickListener {

    private FrPlaysheetBinding binding;
    private PlaySheetViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(PlaySheetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FrPlaysheetBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setModel(model);
        binding.setClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recent) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.recentMusicListFr);
        } else if (v.getId() == R.id.favorite) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.favoriteMusicListFr);
        }
    }
}
