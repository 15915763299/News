package com.demo.c.news.headlinenews;

import com.demo.base.activity.IBaseView;
import com.demo.c.news.beans.Category;

import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2020/3/17 16:30
 * description : VM层调用V层的功能接口，需要Fragment、Activity去实现
 */
public interface HeadlineNewsView extends IBaseView {

    void onCategoryLoaded(ArrayList<Category> channels);

}
