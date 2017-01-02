package com.ggccnu.myjianshu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;
import com.ggccnu.myjianshu.mode.Category;

import java.util.ArrayList;

/**
 * Created by lishaowei on 16/6/7.
 */
public class ArticleFragment extends Fragment {

    private static final String TAG = "ArticleFragment" ;
    /**
     * 分类滑动选项卡
     */
    private PagerSlidingTabStrip mPagerTabs;
    /**
     * 滑动组件
     */
    private ViewPager mPager;
    /**
     * 选项fragment界面
     */
    private ArrayList<Fragment> mCateDetailFragmentList = new ArrayList<>();

    /**
     * 分类数组
     */
    private ArrayList<Category> mCategoryList = new ArrayList<>();

    LinearLayout mLinearLayout;

    public ArticleFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.frag_child_category, container, false);
        mPager = (ViewPager) mLinearLayout.findViewById(R.id.pager_pgMain_child_category);
        mPagerTabs = (PagerSlidingTabStrip) mLinearLayout.findViewById(R.id.tabs_main_child_category);
        //queryCategoryAndArticle();
        initView();
        mPager.setAdapter(new CategoryFragmentAdaptor(getChildFragmentManager(), mCateDetailFragmentList, mCategoryList));
        mPagerTabs.setViewPager(mPager);
        return mLinearLayout;
    }

    private void initView() {
        mCategoryList.add(new Category(0, "发现", "", 0));
        mCategoryList.add(new Category(1, "热门", "", 1));
        mCategoryList.add(new Category(2, "七日热门", "", 0));
        mCategoryList.add(new Category(3, "简书包", "", 1));
        mCategoryList.add(new Category(4, "专栏", "", 0));
        mCategoryList.add(new Category(5, "体验", "", 1));
        // 把子fragment的cid和article传递过去
        for (int i = 0; i < mCategoryList.size(); i++) {
            TopicFragment mTopicFragment = new TopicFragment();
            Bundle mBundle = new Bundle();
            mBundle.putInt("cid", mCategoryList.get(i).getId());
            mTopicFragment.setArguments(mBundle);
            mCateDetailFragmentList.add(mTopicFragment);
        }

    }
}
