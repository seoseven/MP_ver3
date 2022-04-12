package com.nwu.ypf.mp_ver3.ui.basemusic;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;

import com.nwu.ypf.mp_ver3.bean.IMusicInfo;
import com.nwu.ypf.mp_ver3.databinding.RvItemMusicBinding;
import com.nwu.ypf.mp_ver3.ui.common.BaseListAdapter;
import com.nwu.ypf.mp_ver3.ui.common.BaseViewHolder;
import com.nwu.ypf.mp_ver3.util.PlayManager;

class MusicListAdapter extends BaseListAdapter<IMusicInfo, BaseViewHolder<RvItemMusicBinding>> {

    private static class DiffCallback extends DiffUtil.ItemCallback<IMusicInfo> {

        @Override
        public boolean areItemsTheSame(@NonNull IMusicInfo oldItem, @NonNull IMusicInfo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull IMusicInfo oldItem, @NonNull IMusicInfo newItem) {
            return oldItem.equals(newItem);
        }
    }

    private LifecycleOwner lifecycleOwner;

    MusicListAdapter(OnItemClickListener<IMusicInfo> listener, LifecycleOwner lifecycleOwner) {
        super(new DiffCallback(), listener);
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public BaseViewHolder<RvItemMusicBinding> onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RvItemMusicBinding binding = RvItemMusicBinding.inflate(inflater, parent, false);
        binding.setPlayManager(PlayManager.getInstance(parent.getContext()));
        binding.setLifecycleOwner(lifecycleOwner);
        return new BaseViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<RvItemMusicBinding> holder, int position) {
        super.onBindViewHolder(holder, position);
        IMusicInfo data = getItem(position);
        holder.getBinding().setMusicInfo(data);
    }

}
