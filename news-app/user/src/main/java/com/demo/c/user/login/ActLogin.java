package com.demo.c.user.login;

import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.demo.base.LoginUser;
import com.demo.base.activity.MvvmActivity;
import com.demo.base.utils.ToastUtils;
import com.demo.c.user.BR;
import com.demo.c.user.R;
import com.demo.c.user.databinding.ActLoginBinding;
import com.demo.c.user.processer.PUserCenter;
import com.demo.c.user.usercenter.FragUserCenter;


public class ActLogin extends MvvmActivity<ActLoginBinding, LoginViewModel> implements LoginView {

    private static final String TAG = ActLogin.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected LoginViewModel getViewModel() {
        return new LoginViewModel();
    }

    /**
     * 绑定VM中的数据
     */
    @Override
    protected int getBindingVariable() {
        return BR.loginViewModel;
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    public void onLoginSuccess(LoginUser loginUser) {
        Intent intent = getIntent();
        ToastUtils.showShort("登录成功");
        if (intent != null && intent.hasExtra(CCUtil.EXTRA_KEY_CALL_ID)) {
            CCResult result = CCResult.success(PUserCenter.PROCESSOR_NAME, new FragUserCenter());
            CC.sendCCResult(intent.getStringExtra(CCUtil.EXTRA_KEY_CALL_ID), result);
            finish();
        }
    }
}
