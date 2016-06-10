package com.ggccnu.myjianshu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/7.
 */
public class ChildCateFragment extends Fragment {

    private static final int UPDATE_VIEWPAGE = 0;
    private static final String TAG = "ChildCateFragment" ;
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

    private Handler mHandler;
    private Person mPerson;

    LinearLayout mLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.frag_child_category, container, false);
        mPager = (ViewPager) mLinearLayout.findViewById(R.id.pager_pgMain_child_category);
        mPagerTabs = (PagerSlidingTabStrip) mLinearLayout.findViewById(R.id.tabs_main_child_category);
        initView();
        mPager.setAdapter(new CategoryFragmentAdaptor(getFragmentManager(), mCateDetailFragmentList, mCategoryList));
        mPagerTabs.setViewPager(mPager);
        return mLinearLayout;
    }

    private void initView() {

        List<Article> mArticleList = new ArrayList<>();
        mArticleList.add(new Article(0, "Lucy", 0, 2, 5, 1, 3, "日报", "404", "404"));
        mArticleList.add(new Article(1, "Jack", 0, 2, 5, 1, 3, "日报", "404", "404"));
        mCategoryList.add(new Category(0, "child_cate0", "", 0));
        mCategoryList.add(new Category(1, "child_cate1", "", 1));

        for (int i = 0; i < mCategoryList.size(); i++) {
            CateDetailFragment mCateDetailFragment = new CateDetailFragment();
            Bundle mBundle = new Bundle();
            mBundle.putInt("cid", mCategoryList.get(i).getId());
            mBundle.putSerializable("articles", (Serializable) mArticleList);
            mCateDetailFragment.setArguments(mBundle);
            mCateDetailFragmentList.add(mCateDetailFragment);
        }

    }

}
