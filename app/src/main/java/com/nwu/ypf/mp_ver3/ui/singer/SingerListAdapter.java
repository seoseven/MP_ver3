package com.nwu.ypf.mp_ver3.ui.singer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.nwu.ypf.mp_ver3.bean.Singer;
import com.nwu.ypf.mp_ver3.databinding.RvItemSingerBinding;
import com.nwu.ypf.mp_ver3.ui.common.BaseListAdapter;
import com.nwu.ypf.mp_ver3.ui.common.BaseViewHolder;

public class SingerListAdapter extends BaseListAdapter<Singer,
        BaseViewHolder<RvItemSingerBinding>> {

    private static class DiffCallback extends DiffUtil.ItemCallback<Singer> {

        @Override
        public boolean areItemsTheSame(@NonNull Singer oldItem, @NonNull Singer newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Singer oldItem, @NonNull Singer newItem) {
            return oldItem.equals(newItem);
        }
    }

    SingerListAdapter(OnItemClickListener<Singer> listener) {
        super(new DiffCallback(), listener);
    }

    @NonNull
    @Override
    public BaseViewHolder<RvItemSingerBinding> onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RvItemSingerBinding binding = RvItemSingerBinding.inflate(inflater, parent,
                false);
        return new BaseViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<RvItemSingerBinding> holder,
                                 int position) {
        super.onBindViewHolder(holder, position);
        Singer data = getItem(position);
        holder.getBinding().setSinger(data);
    }

}
