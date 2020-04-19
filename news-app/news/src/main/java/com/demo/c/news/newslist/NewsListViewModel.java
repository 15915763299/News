package com.demo.c.news.newslist;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.model.BasePagingModel;
import com.demo.base.viewmodel.MvvmBaseViewModel;

import java.util.ArrayList;

/**
 * 新闻列表 Model
 * 这里直接实现了BasePagingModel.ILoadListener，所以NewsListModel直接register this
 */
public class NewsListViewModel extends MvvmBaseViewModel<NewsListView, NewsListModel>
        implements BasePagingModel.ILoadListener<ArrayList<BaseCustomViewModel>> {

    private ArrayList<BaseCustomViewModel> newsList;

    NewsListViewModel(String categoryId, String categoryName) {
        newsList = new ArrayList<>();
        model = new NewsListModel(categoryId, categoryName);
        model.register(this);
        model.getCachedDataAndLoad();
    }

    @Override
    public void onLoadFinish(BasePagingModel model, ArrayList<BaseCustomViewModel> data,
                             boolean isEmpty, boolean isFirstPage, boolean hasNextPage) {
        if (getView() != null) {
            if (model instanceof NewsListModel) {
                if (isFirstPage) {
                    newsList.clear();
                }
                if (isEmpty) {
                    if (isFirstPage) {
                        getView().onRefreshEmpty();
                    } else {
                        getView().onLoadMoreEmpty();
                    }
                } else {
                    newsList.addAll(data);
                    getView().onNewsLoaded(newsList);
                }
            }
        }
    }

    @Override
    public void onLoadFail(BasePagingModel model, String prompt, boolean isFirstPage) {
        if (getView() != null) {
            if (isFirstPage) {
                getView().onRefreshFailure(prompt);
            } else {
                getView().onLoadMoreFailure(prompt);
            }
        }
    }

    public void tryToRefresh() {
        model.getCachedDataAndLoad();
    }

    public void tryToLoadNextPage() {
        model.loadNexPage();
    }

}
