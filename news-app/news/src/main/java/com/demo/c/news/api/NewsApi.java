package com.demo.c.news.api;

import com.demo.network.Constants;
import com.demo.network.base.BaseApi;
import com.demo.network.base.BaseRes;
import com.demo.network.base.Page;
import com.demo.c.news.beans.Category;
import com.demo.c.news.beans.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;

/**
 * 新闻API
 */
public final class NewsApi extends BaseApi {

    private static class Instance {
        private static NewsApi instance = new NewsApi();
    }

    public static NewsApi get() {
        return Instance.instance;
    }

    private NewsApiInterface newsApiInterface;

    private NewsApi() {
        super(Constants.CLOUD_URL);
        newsApiInterface = retrofit.create(NewsApiInterface.class);
    }

    /**
     * 获取新闻分类类别
     *
     * @param observer 由调用者传过来的观察者对象
     */
    public void getCategories(Observer<BaseRes<List<Category>>> observer) {
        ApiSubscribe(newsApiInterface.getCategories(), observer);
    }

    /**
     * 用于获取新闻列表
     *
     * @param observer   由调用者传过来的观察者对象
     * @param categoryId 分类编号
     * @param pageSize   页长
     * @param pageIndex  页码
     */
    public void getNewsPage(Observer<BaseRes<Page<News>>> observer,
                            String categoryId, String pageSize, String pageIndex) {
        Map<String, String> params = new HashMap<>(3);
        params.put("categoryId", categoryId);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        ApiSubscribe(newsApiInterface.getNewsPage(params), observer);
    }
}
