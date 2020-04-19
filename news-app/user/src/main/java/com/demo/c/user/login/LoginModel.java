package com.demo.c.user.login;

import com.demo.base.BaseApp;
import com.demo.base.LoginUser;
import com.demo.base.model.BaseModel;
import com.demo.c.user.api.UserApi;
import com.demo.network.base.BaseRes;
import com.demo.network.errorhandler.ExceptionHandle;
import com.demo.network.observer.NetBaseObserver;

/**
 * @author 尉迟涛
 * create time : 2020/3/16 19:34
 * description :
 */
public class LoginModel extends BaseModel<LoginUser> {

    private String phone;
    private String password;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void refresh() {

    }

    @Override
    protected void load() {
        UserApi.get().login(new NetBaseObserver<BaseRes<LoginUser>>(this) {
            @Override
            public void onError(ExceptionHandle.ResponseThrowable e) {
                e.printStackTrace();
                loadFail(e.message);
            }

            @Override
            public void onNext(BaseRes<LoginUser> loginUserBaseRes) {
                BaseApp.app.setLoginUser(loginUserBaseRes.data);
            }
        }, phone, password);
    }
}
