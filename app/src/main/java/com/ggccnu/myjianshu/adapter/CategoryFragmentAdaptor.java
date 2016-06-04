package com.ggccnu.myjianshu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ggccnu.myjianshu.fragment.CateDetailFragment;
import com.ggccnu.myjianshu.mode.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class CategoryFragmentAdaptor extends FragmentPagerAdapter {

    private List<CateDetailFragment> mCateDetailFragmentList;
    private ArrayList<Category> mCategoryList;

    public CategoryFragmentAdaptor(FragmentManager fm, List<CateDetailFragment> fragments, ArrayList<Category> category) {
        super(fm);
        this.mCateDetailFragmentList = fragments;
        this.mCategoryList = category;
    }

    @Override
    public Fragment getItem(int position) {
        return mCateDetailFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }
}
