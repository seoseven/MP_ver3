package com.nwu.ypf.mp_ver3.ui.favoritemusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.databinding.FrFavoriteMusicBinding;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListFr;
import com.nwu.ypf.mp_ver3.ui.basemusic.BaseMusicListViewModel;
import com.nwu.ypf.mp_ver3.util.PlayManager;

import java.util.List;

public class FavoriteMusicListFr extends BaseMusicListFr implements View.OnClickListener {

    private FrFavoriteMusicBinding binding;
    private FavoriteMusicViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(FavoriteMusicViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FrFavoriteMusicBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setPlayManager(PlayManager.getInstance(requireContext()));
        binding.setClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionBar(binding.toolbar, true);
        model.getMusicList().observe(getViewLifecycleOwner(),
                new Observer<Source<List<IMusicInfo>>>() {
                    @Override
                    public void onChanged(Source<List<IMusicInfo>> listSource) {
                        binding.setSource(listSource);
                    }
                });
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
