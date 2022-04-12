package com.nwu.ypf.mp_ver3.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Source<T> {

    public static <T> Source<T> loading() {
        return new Source<>(SourceState.Loading);
    }

    public static <T> Source<T> success(T data) {
        return new Source<>(data, SourceState.Success);
    }

    public static <T> Source<T> failed() {
        return new Source<>(SourceState.Failed);
    }

    private @Nullable
    T data;
    private @NonNull
    SourceState state;

    public Source(@NonNull SourceState state) {
        this.state = state;
    }

    public Source(@Nullable T data, @NonNull SourceState state) {
        this.data = data;
        this.state = state;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @NonNull
    public SourceState getState() {
        return state;
    }

    public void setState(@NonNull SourceState state) {
        this.state = state;
    }
}
