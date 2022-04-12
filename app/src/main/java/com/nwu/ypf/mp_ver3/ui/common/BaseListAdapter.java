package com.nwu.ypf.mp_ver3.ui.common;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nwu.ypf.mp_ver3.ui.album.AlbumListAdapter;

public abstract class BaseListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends ListAdapter<T, VH> {

    public interface OnItemClickListener<T> {
        /**
         * 列表项被点击。
         *
         * @param position 点击的表项位置。
         * @param data     对应的数据。
         */
        void onItemClick(int position, T data);
    }

    @Nullable
    private OnItemClickListener onItemClickListener = null;

    protected BaseListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    protected BaseListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback,
                              OnItemClickListener<T> listener) {
        super(diffCallback);
        setOnItemClickListener(listener);
    }

    @CallSuper
    protected void onItemClicked(int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(position, getItem(position));
        }
    }

    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull final VH holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked(holder.getAdapterPosition());
            }
        });
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
