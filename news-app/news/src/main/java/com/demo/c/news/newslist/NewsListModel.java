package com.demo.c.news.newslist;

import android.util.Log;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.model.BasePagingModel;
import com.demo.base.utils.EmptyUtils;
import com.demo.common.views.NewsTitlePictureVM;
import com.demo.common.views.NewsTitleVM;
import com.demo.network.base.BaseRes;
import com.demo.network.base.Page;
import com.demo.network.errorhandler.ExceptionHandle;
import com.demo.network.observer.NetBaseObserver;
import com.demo.c.news.api.NewsApi;
import com.demo.c.news.beans.News;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 新闻分类 Model
 */
public class NewsListModel extends BasePagingModel<ArrayList<BaseCustomViewModel>> {

    private static final String PREF_KEY_NEWS_CHANNEL = "pref_key_news_";
    private static final String PAGE_SIZE = "8";

    private String categoryId;
    private String categoryName;

    @Override
    protected String getCachedPreferenceKey() {
        return PREF_KEY_NEWS_CHANNEL + categoryName;
    }

    protected Type getTClass() {
        return new TypeToken<ArrayList<NewsTitlePictureVM>>() {
        }.getType();
    }

    NewsListModel(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    @Override
    public void refresh() {
        Log.e("NewsListModel", "refresh " + categoryId);
        isRefresh = true;
        load();
    }

    public void loadNexPage() {
        Log.e("NewsListModel", "loadNexPage " + categoryId);
        isRefresh = false;
        load();
    }

    @Override
    protected void load() {
        Log.e("NewsListModel", "load " + categoryId);
        NewsApi.get().getNewsPage(new NetBaseObserver<BaseRes<Page<News>>>(this) {
            @Override
            public void onError(ExceptionHandle.ResponseThrowable e) {
                e.printStackTrace();
                loadFail(e.message, isRefresh);
            }

            @Override
            public void onNext(BaseRes<Page<News>> newsPage) {
                // All observer run on main thread, no need to synchronize
                pageNumber = isRefresh ? 2 : pageNumber + 1;
                ArrayList<BaseCustomViewModel> baseViewModels = new ArrayList<>();

                for (News news : newsPage.data.records) {
                    if (EmptyUtils.isEmpty(news.imageUrl)) {
                        NewsTitleVM viewModel = new NewsTitleVM(
                                news.author,
                                news.title,
                                news.description,
                                news.newsUrl,
                                news.createTime
                        );
                        baseViewModels.add(viewModel);
                    } else {
                        NewsTitlePictureVM viewModel = new NewsTitlePictureVM(
                                news.author,
                                news.title,
                                news.description,
                                news.newsUrl,
                                news.imageUrl,
                                news.createTime
                        );
                        baseViewModels.add(viewModel);
                    }
                }
                loadSuccess(baseViewModels, baseViewModels.size() == 0, isRefresh, baseViewModels.size() == 0);
            }
        }, categoryId, PAGE_SIZE, String.valueOf(isRefresh ? 1 : pageNumber));
    }
}
