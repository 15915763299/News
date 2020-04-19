package com.demo.news.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.demo.base.activity.MvvmActivity;
import com.demo.base.viewmodel.MvvmBaseViewModel;
import com.demo.news.R;
import com.demo.news.databinding.ActHomeBinding;
import com.demo.news.fragment.CategoryFragment;
import com.demo.news.fragment.ServiceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;

public class ActHome extends MvvmActivity<ActHomeBinding, MvvmBaseViewModel> {
    private Fragment homeFragment, userFragment, currentFragment;
    private CategoryFragment mCategoryFragment = new CategoryFragment();
    private ServiceFragment mServiceFragment = new ServiceFragment();

    @Override
    protected int getLayoutId() {
        return R.layout.act_home;
    }

    @Override
    protected MvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callHomeFragment();
        currentFragment = homeFragment;

        disableShiftMode(viewDataBinding.bottomView);
        viewDataBinding.bottomView.setOnNavigationItemSelectedListener((@NonNull MenuItem menuItem) -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.menu_home:
                    fragment = homeFragment;
                    break;
                case R.id.menu_categories:
                    fragment = mCategoryFragment;
                    break;
                case R.id.menu_services:
                    fragment = mServiceFragment;
                    break;
                case R.id.menu_account:
                    if (userFragment == null) {
                        callUserFragment();
                    }
                    fragment = userFragment;
                    break;
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(menuItem.getTitle());
            }
            switchFragment(currentFragment, fragment);
            currentFragment = fragment;
            return true;
        });
        viewDataBinding.bottomView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewDataBinding.container.getId(), homeFragment);
        transaction.commit();

        showBadgeView(3, 5);
    }

    /**
     * 主页
     */
    private void callHomeFragment() {
        CCResult result = CC.obtainBuilder("News")
                .setActionName("HeadlineNews")
                .build().call();
        homeFragment = (Fragment) result.getDataMap().get("HeadlineNews");
    }

    /**
     * 用户页
     */
    private void callUserFragment() {
        CCResult result = CC.obtainBuilder("User")
                .setActionName("UserCenter")
                .build().call();
        userFragment = (Fragment) result.getDataMap().get("UserCenter");
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = manger.beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from);
                }
                transaction.add(R.id.container, to).commit();
            } else {
                if (from != null) {
                    transaction.hide(from);
                }
                transaction.show(to).commit();
            }
        }
    }

    /**
     * BottomNavigationView显示角标
     *
     * @param viewIndex  tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    private void showBadgeView(int viewIndex, int showNumber) {
        // 具体child的查找和view的嵌套结构请在源码中查看
        // 从bottomNavigationView中获得BottomNavigationMenuView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) viewDataBinding.bottomView.getChildAt(0);
        // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
        if (viewIndex < menuView.getChildCount()) {
            // 获得viewIndex对应子tab
            View view = menuView.getChildAt(viewIndex);
            // 从子tab中获得其中显示图片的ImageView
            View icon = view.findViewById(com.google.android.material.R.id.icon);
            // 获得图标的宽度
            int iconWidth = icon.getWidth();
            // 获得tab的宽度/2
            int tabWidth = view.getWidth() / 2;
            // 计算badge要距离右边的距离
            int spaceWidth = tabWidth - iconWidth;

            // 显示badegeview
            new QBadgeView(this).bindTarget(view).setGravityOffset(spaceWidth + 50, 13, false).setBadgeNumber(showNumber);
        }
    }

    /**
     * BottomView 超过三个标签不显示图片问题
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
     */
    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                // item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }
}
