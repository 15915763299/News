package com.demo.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.demo.base.customview.BaseCustomView;
import com.demo.common.R;
import com.demo.common.databinding.ItemNewsTitleBinding;
import com.demo.common.webview.WebActivity;

/**
 * 新闻主页Item
 */
public class NewsTitleView extends BaseCustomView<ItemNewsTitleBinding, NewsTitleVM> {
    public NewsTitleView(Context context) {
        super(context);
    }

    public NewsTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.item_news_title;
    }

    @Override
    public void setDataToView(NewsTitleVM data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        WebActivity.startCommonWeb(view.getContext(), "", getViewModel().newsUrl);
    }
}
