package com.ggccnu.myjianshu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ggccnu.myjianshu.mode.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class CategoryFragmentAdaptor extends FragmentPagerAdapter {

    private List<Fragment> mCateDetailFragmentList;
    private ArrayList<Category> mCategoryList;

    public CategoryFragmentAdaptor(FragmentManager fm, List<Fragment> fragments, ArrayList<Category> category) {
        super(fm);
        this.mCateDetailFragmentList = fragments;
        this.mCategoryList = category;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mCateDetailFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    public String getPageTitle(int i) {
        return mCategoryList.get(i).getName();
    }
}
