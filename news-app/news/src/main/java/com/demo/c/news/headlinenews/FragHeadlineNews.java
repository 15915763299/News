package com.demo.c.news.headlinenews;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.demo.base.fragment.MvvmFragment;
import com.demo.c.news.R;
import com.demo.c.news.beans.Category;
import com.demo.c.news.databinding.FragHomeBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * 主页头条Fragment
 * 1、继承MvvmFragment
 * （1）FragHomeBinding为databiding自动生成，绑定xml中的View，与FragHeadlineNews共同组成V层
 * （2）HeadlineNewsViewModel为自定义VM层，连接V层与M层
 * 2、实现HeadlineNewsViewModel.IMainView接口，定义了V层的数据加载行为
 */
public class FragHeadlineNews extends MvvmFragment<FragHomeBinding, HeadlineNewsViewModel> implements HeadlineNewsView {

    private HeadlineNewsFragmentAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.refresh();
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initChannels();
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_home;
    }

    @Override
    public HeadlineNewsViewModel getViewModel() {
        return new HeadlineNewsViewModel();
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected String getFragmentTag() {
        return "FragHeadlineNews";
    }

    private void initChannels() {
        mAdapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.viewpager.setAdapter(mAdapter);
        viewDataBinding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(viewDataBinding.tablayout));
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        //绑定tab点击事件
        viewDataBinding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewDataBinding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onCategoryLoaded(ArrayList<Category> categories) {
        mAdapter.setCategories(categories);

        TabLayout tabLayout = viewDataBinding.tablayout;
        tabLayout.removeAllTabs();
        for (Category category : categories) {
            tabLayout.addTab(tabLayout.newTab().setText(category.categoryName));
        }
        tabLayout.scrollTo(0, 0);
    }
}
