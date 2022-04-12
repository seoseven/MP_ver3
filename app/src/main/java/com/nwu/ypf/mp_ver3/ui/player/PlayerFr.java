package com.nwu.ypf.mp_ver3.ui.player;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.databinding.FrPlayerBinding;
import com.nwu.ypf.mp_ver3.ui.common.BaseFr;
import com.nwu.ypf.mp_ver3.util.PlayManager;
import com.nwu.ypf.mp_ver3.util.glide.GlideApp;

public class PlayerFr extends BaseFr implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,Dialog.OnShowListener {

    private FrPlayerBinding binding;
    private PlayerViewModel model;
    private PlayManager playManager;

    private int stateBarColorBackup = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(PlayerViewModel.class);
        playManager = PlayManager.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        stateBarColorBackup = requireCompatAty().getWindow().getStatusBarColor();
        binding = FrPlayerBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setState(playManager.playState);
        binding.setMusicInfo(playManager.playingMusic);
        binding.setPlayPosition(playManager.playPosition);
        binding.setSwitchStrategy(playManager.strategy);
        binding.setClickListener(this);
        binding.setPlayManager(PlayManager.getInstance(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setTitle("");
        setupActionBar(binding.toolbar, true);
        binding.seekBar.setOnSeekBarChangeListener(this);

        model.isFavorite.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFavorite) {
                setFavoriteState(isFavorite);
            }
        });

        playManager.playingMusic.observe(getViewLifecycleOwner(), new Observer<IMusicInfo>() {
            @Override
            public void onChanged(IMusicInfo musicInfo) {
                GlideApp.with(PlayerFr.this)
                        .load(musicInfo)
                        .error(R.drawable.img_default_artist_album)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                        Target<Drawable> target, boolean isFirstResource) {
                                int color = Color.parseColor("#616161");
                                binding.rootLayout.setBackgroundColor(color);
                                requireCompatAty().getWindow().setStatusBarColor(color);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model,
                                                           Target<Drawable> target,
                                                           DataSource dataSource,
                                                           boolean isFirstResource) {
                                Palette.from(((BitmapDrawable) resource).getBitmap())
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(@Nullable Palette palette) {
                                                int def = Color.parseColor("#B0D8E0");
                                                int result = def;
                                                if (palette != null) {
                                                    result = palette.getLightMutedColor(def);
                                                }
                                                binding.rootLayout.setBackgroundColor(result);
                                                requireCompatAty().getWindow().setStatusBarColor(result);
                                            }
                                        });
                                return false;
                            }
                        })
                        .into(binding.musicAlbum);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireCompatAty().getWindow().setStatusBarColor(stateBarColorBackup);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.favorite.getId()) {
            model.toggleFavorite();
        } else if (v.getId() == binding.menu.getId()) {
            new MusicDetailDialog()
                    .setMusicInfo(PlayManager.getInstance(requireContext()).playingMusic)
                    .show(getChildFragmentManager(), "Music detail");
        } else if (v.getId() == binding.timer.getId()) {
            setTimer();
        }
    }

    private void setTimer() {

        Long schedule = playManager.autoStopTime.getValue();
        String[] items;
        if (schedule != null && schedule != 0 && schedule > SystemClock.uptimeMillis()) {
            // 当前定时有效
            items = getResources().getStringArray(R.array.timer_options_on);
        } else {
            items = getResources().getStringArray(R.array.timer_options_off);
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.timer_title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long delay = -1;
                        switch (which) {
                            case 0:
                                delay = 1000 * 60 * 5;
                                break;
                            case 1:
                                delay = 1000 * 60 * 10;
                                break;
                            case 2:
                                delay = 1000 * 60 * 15;
                                break;
                            case 3:
                                delay = 0;
                                break;
                        }
                        if (delay > 0) {
                            playManager.setAutoStopTime(SystemClock.uptimeMillis() + delay);
                        } else if (delay == 0) {
                            playManager.cancelAutoStop();
                        }
                    }
                })
                .show();
    }

    private void setFavoriteState(@Nullable Boolean isFavorite) {
        if (isFavorite == null || !isFavorite) {
            binding.favorite.setImageResource(R.drawable.ic_player_favorite_off);
        } else {
            binding.favorite.setImageResource(R.drawable.ic_player_favorite_on);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            playManager.setPlayPosition(requireContext(), progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onShow(DialogInterface dialog) {
        
    }
}
