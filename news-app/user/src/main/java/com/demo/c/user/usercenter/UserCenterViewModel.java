package com.demo.c.user.usercenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.demo.base.model.BaseModel;
import com.demo.base.viewmodel.MvvmBaseViewModel;
import com.demo.c.user.R;
import com.demo.c.user.login.ActLogin;

/**
 * @author 尉迟涛
 * create time : 2020/4/19 21:12
 * description :
 */
public class UserCenterViewModel extends MvvmBaseViewModel<FragUserCenter, BaseModel> {

    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            if (getView() != null && getView().getContext() != null) {
                Context context = getView().getContext();
                Intent intent = new Intent(context, ActLogin.class);
                context.startActivity(intent);
            }
        }
    }

}
