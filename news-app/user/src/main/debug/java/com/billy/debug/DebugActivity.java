package com.billy.debug;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.c.user.login.ActLogin;

/**
 * @author 尉迟涛
 * create time : 2020/3/11 16:59
 * description : https://qibilly.com/CC-website/#/integration-create-component
 */
public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, ActLogin.class));

        // 需要单独安装运行，但不需要入口页面（只需要从主app中调用此组件）时，
        // 可直接finish当前activity
        // finish();
    }
}