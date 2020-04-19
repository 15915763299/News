package com.demo.c.news.headlinenews;

import com.demo.base.model.BaseModel;
import com.demo.base.utils.ToastUtils;
import com.demo.base.viewmodel.MvvmBaseViewModel;
import com.demo.c.news.beans.Category;

import java.util.ArrayList;

/**
 * 新闻主页 Model
 * MvvmBaseViewModel<HeadlineNewsViewModel.IMainView, CategoryModel>
 * IMainView --> 用接口定义V层
 */
public class HeadlineNewsViewModel extends MvvmBaseViewModel<HeadlineNewsView, CategoryModel>
        implements BaseModel.ILoadListener<ArrayList<Category>> {

    HeadlineNewsViewModel() {
        // 创建Model
        model = new CategoryModel();
        // 注册回调，异步回调V层接口
        model.register(this);
        model.getCachedDataAndLoad();
    }

    public void refresh() {
        model.getCachedDataAndLoad();
    }

    @Override
    public void onLoadFinish(BaseModel model, ArrayList<Category> data) {
        if (model instanceof CategoryModel) {
            if (getView() != null && data != null) {
                getView().onCategoryLoaded(data);
            }
        }
    }

    @Override
    public void onLoadFail(BaseModel model, String prompt) {
        ToastUtils.showShort(prompt);
    }
}
