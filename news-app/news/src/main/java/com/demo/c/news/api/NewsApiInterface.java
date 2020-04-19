package com.demo.c.news.api;

import com.demo.network.base.BaseRes;
import com.demo.network.base.Page;
import com.demo.c.news.beans.Category;
import com.demo.c.news.beans.News;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * 新闻请求接口
 */
public interface NewsApiInterface {

    @GET("news/categories")
    Observable<BaseRes<List<Category>>> getCategories();

    @GET("news/newsPage")
    Observable<BaseRes<Page<News>>> getNewsPage(@QueryMap Map<String, String> options);

}
