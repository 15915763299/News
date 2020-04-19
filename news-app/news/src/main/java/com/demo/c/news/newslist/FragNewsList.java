package com.demo.c.news.newslist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.fragment.MvvmFragment;
import com.demo.c.news.R;
import com.demo.c.news.databinding.FragNewsBinding;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

import java.util.ArrayList;

/**
 * 头条分类Fragment
 */
public class FragNewsList extends MvvmFragment<FragNewsBinding, NewsListViewModel>
        implements NewsListView {

    private final static String BUNDLE_KEY_PARAM_CATEGORY_ID = "bundle_key_param_category_id";
    private final static String BUNDLE_KEY_PARAM_CATEGORY_NAME = "bundle_key_param_category_name";

    private NewsListRecyclerViewAdapter adapter;
    private String categoryId = "";
    private String categoryName = "";

    public static FragNewsList newInstance(String categoryId, String categoryName) {
        FragNewsList fragment = new FragNewsList();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CATEGORY_ID, categoryId);
        bundle.putString(BUNDLE_KEY_PARAM_CATEGORY_NAME, categoryName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_news;
    }

    /**
     * 在父类的onViewCreated中执行
     * adapter在NewsListViewModel的构造方法中创建
     */
    @Override
    public NewsListViewModel getViewModel() {
        Log.e(this.getClass().getSimpleName(), this + ": createViewModel.");
        return new NewsListViewModel(categoryId, categoryName);
    }

    @Override
    protected void initParameters() {
        if (getArguments() != null) {
            categoryId = getArguments().getString(BUNDLE_KEY_PARAM_CATEGORY_ID);
            categoryName = getArguments().getString(BUNDLE_KEY_PARAM_CATEGORY_NAME);
            mFragmentTag = categoryName;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if (context != null) {
            initRecycleView(viewDataBinding.recyclerView, context);
            initRefreshLayout(viewDataBinding.refreshLayout, context);
        }
        showLoading();
    }

    private void initRecycleView(RecyclerView recyclerView, Context context) {
        adapter = new NewsListRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.recycler_view_divider);
        if (drawable != null) {
            DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            divider.setDrawable(drawable);
            recyclerView.addItemDecoration(divider);
        }
    }

    private void initRefreshLayout(SmartRefreshLayout refreshLayout, Context context) {
        refreshLayout.setRefreshHeader(new WaterDropHeader(context));
        refreshLayout.setRefreshFooter(new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(refreshlayout ->
                viewModel.tryToRefresh()
        );
        refreshLayout.setOnLoadMoreListener(refreshlayout ->
                viewModel.tryToLoadNextPage()
        );
        setLoadSir(refreshLayout);
    }

    @Override
    public void onNewsLoaded(ArrayList<BaseCustomViewModel> newsItems) {
        if (newsItems != null && newsItems.size() > 0) {
            viewDataBinding.refreshLayout.finishLoadMore();
            viewDataBinding.refreshLayout.finishRefresh();
            viewDataBinding.refreshLayout.setEnableLoadMore(true);
            showContent();
            adapter.setData(newsItems);
        } else {
            onRefreshEmpty();
        }
    }

    /**
     * 重试按钮点击
     */
    protected void onRetryBtnClick() {
        viewModel.tryToRefresh();
    }

    @Override
    protected String getFragmentTag() {
        return categoryName;
    }

    @Override
    public void onLoadMoreEmpty() {
        super.onLoadMoreEmpty();
        viewDataBinding.refreshLayout.finishLoadMore();
        viewDataBinding.refreshLayout.setEnableLoadMore(false);
    }
}
