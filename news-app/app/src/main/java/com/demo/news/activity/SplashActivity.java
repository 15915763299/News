package com.demo.news.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 利用给SplashActivity设置带有背景的SplashTheme来避免启动白屏的问题
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, ActHome.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
