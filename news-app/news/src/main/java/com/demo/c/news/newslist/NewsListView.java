package com.demo.c.news.newslist;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.fragment.IBasePagingView;

import java.util.ArrayList;

/**
 * @author 尉迟涛
 * create time : 2020/3/17 16:33
 * description :
 */
public interface NewsListView extends IBasePagingView {

    void onNewsLoaded(ArrayList<BaseCustomViewModel> channels);

}
