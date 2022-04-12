package com.nwu.ypf.mp_ver3.ui.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private T binding;

    public BaseViewHolder(@NonNull T binding) {
        this(binding.getRoot());
        this.binding = binding;
    }

    private BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public T getBinding() {
        return binding;
    }
}
