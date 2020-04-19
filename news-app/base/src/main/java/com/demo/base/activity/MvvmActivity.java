package com.demo.base.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.demo.base.loadsir.ErrorCallback;
import com.demo.base.loadsir.LottieEmptyCallback;
import com.demo.base.loadsir.LottieLoadingCallback;
import com.demo.base.viewmodel.MvvmBaseViewModel;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * V层-Activity
 */
public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel>
        extends AppCompatActivity implements IBaseView {

    protected VM viewModel;
    protected V viewDataBinding;
    /**
     * 网络加载页面切换
     */
    private LoadService mLoadService;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract VM getViewModel();

    /**
     * 设置XML中data标签中的数据
     */
    protected abstract int getBindingVariable();

    /**
     * 网络失败重试
     */
    protected abstract void onRetryBtnClick();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
    }

    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (viewModel == null) {
            viewModel = getViewModel();
        }
        // 把数据放在ViewModel，绑定View
        if (getBindingVariable() != -1) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
    }

    /**
     * 显示网络当前状态
     * 返回为空，{@link MvvmActivity#onRefreshEmpty}
     * 请求失败，{@link MvvmActivity#onRefreshFailure}
     * 加载中，{@link MvvmActivity#showLoading}
     * 加载完成，{@link MvvmActivity#showContent}
     *
     * @param view 网络错误要显示的View，可以替换这个View来显示
     */
    public void setLoadSir(View view) {
        // You can change the callback on sub thread directly. OnReloadListener
        mLoadService = LoadSir.getDefault().register(view, (View v) -> onRetryBtnClick());
    }

    @Override
    public void onRefreshEmpty() {
        if (mLoadService != null) {
            mLoadService.showCallback(LottieEmptyCallback.class);
        }
    }

    @Override
    public void onRefreshFailure(String message) {
        if (mLoadService != null) {
            mLoadService.showCallback(ErrorCallback.class);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LottieLoadingCallback.class);
        }
    }

    @Override
    public void showContent() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

}
