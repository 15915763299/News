package com.demo.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.demo.base.customview.BaseCustomView;
import com.demo.common.R;
import com.demo.common.databinding.ItemNewsPictureTitleBinding;
import com.demo.common.webview.WebActivity;

/**
 * 新闻主页Item
 * view都放在common中，以便复用
 */
public class NewsTitlePictureView extends BaseCustomView<ItemNewsPictureTitleBinding, NewsTitlePictureVM> {
    public NewsTitlePictureView(Context context) {
        super(context);
    }

    public NewsTitlePictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.item_news_picture_title;
    }

    @Override
    public void setDataToView(NewsTitlePictureVM data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        WebActivity.startCommonWeb(view.getContext(), "", getViewModel().newsUrl);
    }
}
