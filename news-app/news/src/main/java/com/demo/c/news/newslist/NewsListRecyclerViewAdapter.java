package com.demo.c.news.newslist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.recyclerview.BaseViewHolder;
import com.demo.common.views.NewsTitlePictureVM;
import com.demo.common.views.NewsTitlePictureView;
import com.demo.common.views.NewsTitleView;

import java.util.ArrayList;

/**
 * 这里BaseViewHolder<S>的S可能是
 * NewsTitlePictureView或者NewsTitleView（多种Item）
 * 所以这里uncheck不用管它
 */
public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<BaseCustomViewModel> mItems;
    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;

    NewsListRecyclerViewAdapter() {
    }

    void setData(ArrayList<BaseCustomViewModel> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mItems != null && mItems.size() > 0) {
            return mItems.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof NewsTitlePictureVM) {
            return VIEW_TYPE_PICTURE_TITLE;
        } else /*if (mItems.get(position) instanceof NewsTitleVM)*/ {
            return VIEW_TYPE_TITLE;
        }
    }

    /**
     * 多种Item，这里会报uncheck黄色提示
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PICTURE_TITLE) {
            NewsTitlePictureView pictureTitleView = new NewsTitlePictureView(parent.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pictureTitleView.setLayoutParams(layoutParams);
            return new BaseViewHolder(pictureTitleView);
        } else /*if (viewType == VIEW_TYPE_TITLE)*/ {
            NewsTitleView titleView = new NewsTitleView(parent.getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleView.setLayoutParams(layoutParams);
            return new BaseViewHolder(titleView);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }
}
