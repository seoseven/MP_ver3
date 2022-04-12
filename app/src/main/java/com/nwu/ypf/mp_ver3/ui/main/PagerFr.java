package com.nwu.ypf.mp_ver3.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.databinding.FrPagerBinding;
import com.nwu.ypf.mp_ver3.ui.album.AlbumFr;
import com.nwu.ypf.mp_ver3.ui.common.BaseFr;
import com.nwu.ypf.mp_ver3.ui.music.MusicListFr;
import com.nwu.ypf.mp_ver3.ui.playsheet.PlaySheetFr;
import com.nwu.ypf.mp_ver3.ui.singer.SingerFr;
import com.nwu.ypf.mp_ver3.util.PlayManager;

public class PagerFr extends BaseFr implements View.OnClickListener {
    private static final int MSG_QUERY = 1;

    private int[] tabTitles;
    private FrPagerBinding binding;
    private PagerViewModel model;

    private SearchView searchView;
    private SearchResultFr searchResultFr;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_QUERY) {
                // 执行搜索
                if (searchResultFr != null) {
                    searchResultFr.performSearch((String) msg.obj);
                }
            }
        }
    };

    private OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
            if (searchView != null && !searchView.isIconified()) {
                searchView.onActionViewCollapsed();
                model.destroySearchResult();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(PagerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        model.destroySearchResult();
        binding = FrPagerBinding.inflate(inflater, container, false);
        tabTitles = new int[]{R.string.pager_music, R.string.pager_singer, R.string.pager_album, R.string.pager_playsheet};
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setModel(model);
        PlayManager playManager = PlayManager.getInstance(requireContext());
        binding.setMusicInfo(playManager.playingMusic);
        binding.setState(playManager.playState);
        binding.setClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireCompatAty().getOnBackPressedDispatcher().addCallback(backPressedCallback);
        binding.viewPager.setAdapter(new PagerAdapter(this));
        setupActionBar(binding.toolbar, false);
        requireCompatAty().getSupportActionBar().setTitle(R.string.app_name);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                    }
                }).attach();

        // 搜索结果页面状态监听
        model.showSearchResult.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showSearchResult) {
                if (showSearchResult == null || !showSearchResult) {
                    backPressedCallback.setEnabled(false);
                    if (searchResultFr != null) {
                        getChildFragmentManager().beginTransaction()
                                .remove(searchResultFr).commit();
                        searchResultFr = null;
                    }
                } else {
                    backPressedCallback.setEnabled(true);
                    if (searchResultFr == null) {
                        searchResultFr = new SearchResultFr();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.frame, searchResultFr)
                                .commit();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pager, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.pager_search));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.showSearchResult();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                model.destroySearchResult();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeMessages(MSG_QUERY);
                Message msg = handler.obtainMessage(MSG_QUERY, newText);
                handler.sendMessageDelayed(msg, 500L);
                return false;
            }
        });
    }

    private static class PagerAdapter extends FragmentStateAdapter {

        PagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new MusicListFr();
                case 1:
                    return new SingerFr();
                case 2:
                    return new AlbumFr();
                case 3:
                    return new PlaySheetFr();

            }
            throw new RuntimeException("Unknown pager position!");
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.miniPlayer.getRoot().getId()) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_pagerFr_to_playerFr);
        }
    }
}
