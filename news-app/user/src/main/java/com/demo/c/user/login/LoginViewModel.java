package com.demo.c.user.login;

import android.view.View;

import com.demo.base.LoginUser;
import com.demo.base.model.BaseModel;
import com.demo.base.utils.EmptyUtils;
import com.demo.base.utils.ToastUtils;
import com.demo.base.viewmodel.MvvmBaseViewModel;
import com.demo.c.user.R;

/**
 * @author 尉迟涛
 * create time : 2020/3/16 19:26
 * description :
 */
public class LoginViewModel extends MvvmBaseViewModel<LoginView, LoginModel>
        implements BaseModel.ILoadListener<LoginUser> {

    /**
     * VM层的参数会注入到xml中，双向绑定
     * 写成public，不然就写get/set方法
     */
    public String phone;
    public String password;

    public LoginViewModel() {
        model = new LoginModel();
        model.register(this);
    }

    public void login() {
        if (EmptyUtils.isEmpty(phone)) {
            ToastUtils.showShort("手机号不能为空");
        } else if (EmptyUtils.isEmpty(password)) {
            ToastUtils.showShort("密码不能为空");
        }
        model.setPhone(phone);
        model.setPassword(password);
        model.load();
    }

    @Override
    public void onLoadFinish(BaseModel model, LoginUser data) {
        if (model instanceof LoginModel) {
            if (getView() != null && data != null) {
                getView().onLoginSuccess(data);
            }
        }
    }

    @Override
    public void onLoadFail(BaseModel model, String prompt) {

    }

    /**
     * MVVM有三种点击事件的绑定方式参考如下：
     * https://www.jianshu.com/p/b2a27c57f4d0
     */
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            login();
        }
    }

}
