package com.ggccnu.myjianshu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ggccnu.myjianshu.mode.ViewPagerSlide;

import java.util.List;

/**
 * Created by lishaowei on 16/6/19.
 */
public class ViewPagerFragAdapter extends FragmentPagerAdapter {

    private List<ViewPagerSlide> mViewPagerSlideList;

    public ViewPagerFragAdapter(FragmentManager fm, List<ViewPagerSlide> viewPagerSlideList) {
        super(fm);
        mViewPagerSlideList = viewPagerSlideList;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PictureFragment(mViewPagerSlideList.get(position));

        return fragment;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mViewPagerSlideList.size();
    }
}
