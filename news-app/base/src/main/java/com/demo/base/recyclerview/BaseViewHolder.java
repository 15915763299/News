package com.demo.base.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.customview.ICustomView;


/**
 *
 */
public class BaseViewHolder<S extends BaseCustomViewModel> extends RecyclerView.ViewHolder {
    private ICustomView<S> view;

    public BaseViewHolder(ICustomView<S> view) {
        super((View) view);
        this.view = view;
    }

    public void bind(@NonNull S item) {
        view.setData(item);
    }
}