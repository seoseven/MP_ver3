package com.nwu.ypf.mp_ver3.ui.album;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.nwu.ypf.mp_ver3.bean.Album;
import com.nwu.ypf.mp_ver3.databinding.RvItemAlbumBinding;
import com.nwu.ypf.mp_ver3.ui.common.BaseListAdapter;
import com.nwu.ypf.mp_ver3.ui.common.BaseViewHolder;

public class AlbumListAdapter extends BaseListAdapter<Album, BaseViewHolder<RvItemAlbumBinding>> {

    private static class DiffCallback extends DiffUtil.ItemCallback<Album> {

        @Override
        public boolean areItemsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
            return oldItem.equals(newItem);
        }
    }

    AlbumListAdapter(OnItemClickListener<Album> listener) {
        super(new DiffCallback(), listener);
    }

    @NonNull
    @Override
    public BaseViewHolder<RvItemAlbumBinding> onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RvItemAlbumBinding binding = RvItemAlbumBinding.inflate(inflater, parent,
                false);
        return new BaseViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<RvItemAlbumBinding> holder,
                                 int position) {
        super.onBindViewHolder(holder, position);
        Album data = getItem(position);
        holder.getBinding().setAlbum(data);
    }

}
