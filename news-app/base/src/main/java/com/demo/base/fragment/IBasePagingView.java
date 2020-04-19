package com.demo.base.fragment;

import com.demo.base.activity.IBaseView;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public interface IBasePagingView extends IBaseView {

    void onLoadMoreFailure(String message);

    void onLoadMoreEmpty();
}
