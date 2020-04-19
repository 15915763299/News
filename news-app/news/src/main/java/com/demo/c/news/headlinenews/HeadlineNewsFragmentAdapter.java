package com.demo.c.news.headlinenews;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.c.news.beans.Category;
import com.demo.c.news.newslist.FragNewsList;

import java.util.ArrayList;

/**
 * 新闻主页 FragmentAdapter
 */
public class HeadlineNewsFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Category> categories;

    HeadlineNewsFragmentAdapter(FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int pos) {
        return FragNewsList.newInstance(categories.get(pos).categoryId, categories.get(pos).categoryName);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (categories != null && categories.size() > 0) {
            return categories.size();
        }
        return 0;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
    }
}