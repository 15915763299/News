package com.demo.c.user.usercenter;

import com.demo.base.fragment.MvvmFragment;
import com.demo.c.user.BR;
import com.demo.c.user.R;
import com.demo.c.user.databinding.FragUserCenterBinding;

/**
 *
 */
public class FragUserCenter extends MvvmFragment<FragUserCenterBinding, UserCenterViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.frag_user_center;
    }

    @Override
    protected UserCenterViewModel getViewModel() {
        return new UserCenterViewModel();
    }

    @Override
    protected int getBindingVariable() {
        return BR.userCenterViewModel;
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected String getFragmentTag() {
        return null;
    }

}
