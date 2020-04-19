package com.demo.c.news.headlinenews;

import com.demo.base.model.BaseModel;
import com.demo.base.utils.ConvertUtils;
import com.demo.network.base.BaseRes;
import com.demo.network.errorhandler.ExceptionHandle;
import com.demo.network.observer.NetBaseObserver;
import com.demo.c.news.api.NewsApi;
import com.demo.c.news.beans.Category;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻分类M层
 */
public class CategoryModel extends BaseModel<ArrayList<Category>> {
    private static final String PREF_KEY_HOME_CHANNEL = "pref_key_home_channel";

    @Override
    protected String getCachedPreferenceKey() {
        return PREF_KEY_HOME_CHANNEL;
    }

    @Override
    protected Type getTClass() {
        return new TypeToken<ArrayList<Category>>() {
        }.getType();
    }

    @Override
    public void refresh() {
        load();
    }

    /**
     * 自定义更新策略，大于一天才更新
     */
    @Override
    protected boolean isNeedToUpdate(long updateTimeInMills) {
        long current = System.currentTimeMillis();
        return current - updateTimeInMills > ConvertUtils.ONE_DAY;
    }

    @Override
    protected void load() {
        NewsApi.get().getCategories(
                new NetBaseObserver<BaseRes<List<Category>>>(this) {
                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable e) {
                        loadFail(e.message);
                    }

                    @Override
                    public void onNext(BaseRes<List<Category>> listBaseRes) {
                        loadSuccess((ArrayList<Category>) listBaseRes.data);
                    }
                }
        );
    }

}
