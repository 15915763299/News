package com.billy.debug;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.demo.c.news.headlinenews.FragHeadlineNews;

/**
 * @author 尉迟涛
 * create time : 2020/3/11 16:59
 * description : https://qibilly.com/CC-website/#/integration-create-component
 */
public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: 1.获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //TODO: 2.开启一个fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //TODO: 3.向FrameLayout容器添加MainFragment,现在并未真正执行
        transaction.add(android.R.id.content, new FragHeadlineNews(), FragHeadlineNews.class.getName());
        //TODO: 4.提交事务，真正去执行添加动作
        transaction.commit();

        // 需要单独安装运行，但不需要入口页面（只需要从主app中调用此组件）时，
        // 可直接finish当前activity
        // finish();
    }
}