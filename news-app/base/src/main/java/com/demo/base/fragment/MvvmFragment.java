package com.demo.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.demo.base.R;
import com.demo.base.loadsir.ErrorCallback;
import com.demo.base.loadsir.LottieEmptyCallback;
import com.demo.base.loadsir.LottieLoadingCallback;
import com.demo.base.utils.ToastUtils;
import com.demo.base.viewmodel.IMvvmBaseViewModel;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * V层-Fragment
 */
public abstract class MvvmFragment<V extends ViewDataBinding, VM extends IMvvmBaseViewModel> extends Fragment
        implements IBasePagingView {

    protected VM viewModel;
    protected V viewDataBinding;
    /**
     * 网络加载页面切换
     */
    private LoadService mLoadService;
    protected String mFragmentTag = "";

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

    protected abstract String getFragmentTag();

    /***
     *   初始化参数
     */
    protected void initParameters() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getViewModel();
        if (viewModel != null) {
            // 请使用Fragment实现View层接口
            viewModel.attachView(this);
        }
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //***************************************************************************
    // 网络加载页面替换
    //***************************************************************************

    private boolean isShowedContent = false;

    public void setLoadSir(View view) {
        // You can change the callback on sub thread directly.
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
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
            if (!isShowedContent) {
                mLoadService.showCallback(ErrorCallback.class);
            } else {
                ToastUtils.showShort(message);
            }
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
            isShowedContent = true;
            mLoadService.showSuccess();
        }
    }

    @Override
    public void onLoadMoreFailure(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtils.showShort(getString(R.string.no_more_data));
    }


    //***************************************************************************
    // 其他生命周期
    //***************************************************************************

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(mFragmentTag, this + ": " + "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        Log.d(mFragmentTag, this + ": " + "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (viewModel != null && viewModel.isViewAttached())
            viewModel.detachView();
        Log.d(mFragmentTag, this + ": " + "onDetach");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(mFragmentTag, this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(mFragmentTag, this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(mFragmentTag, this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        Log.d(mFragmentTag, this + ": " + "onDestroy");
        super.onDestroy();
    }

}
