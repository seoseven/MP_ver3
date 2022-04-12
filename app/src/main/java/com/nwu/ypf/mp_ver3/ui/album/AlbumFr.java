package com.nwu.ypf.mp_ver3.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.nwu.ypf.mp_ver3.R;
import com.nwu.ypf.mp_ver3.bean.Album;
import com.nwu.ypf.mp_ver3.bean.Source;
import com.nwu.ypf.mp_ver3.bean.SourceState;
import com.nwu.ypf.mp_ver3.databinding.FrAlbumBinding;
import com.nwu.ypf.mp_ver3.ui.common.BaseListAdapter;
import com.nwu.ypf.mp_ver3.ui.main.SharedViewModel;

import java.util.List;

public class AlbumFr extends Fragment implements BaseListAdapter.OnItemClickListener<Album> {

    private FrAlbumBinding binding;
    private SharedViewModel sharedModel;
    private AlbumListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FrAlbumBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new AlbumListAdapter(this);
        binding.albumList.setAdapter(adapter);

        sharedModel.albumList.observe(getViewLifecycleOwner(), new Observer<Source<List<Album>>>() {
            @Override
            public void onChanged(Source<List<Album>> listSource) {
                binding.setSource(listSource);
                if (listSource.getState() == SourceState.Success) {
                    adapter.submitList(listSource.getData());
                }
            }
        });
    }

    @Override
    public void onItemClick(int position, Album data) {
        sharedModel.setCurrentAlbum(data);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.albumMusicFr);
    }
}
