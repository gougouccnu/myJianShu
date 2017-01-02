package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;
import com.ggccnu.myjianshu.fragment.ArticleFragment;
import com.ggccnu.myjianshu.fragment.TopicFragment;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.utils.ArticleCallbackListener;
import com.ggccnu.myjianshu.utils.BmobRequestUtil;
import com.ggccnu.myjianshu.widget.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/18.
 */
public class FaxianFragment extends BaseFragment {

    private static final String TAG = "FaxianFragment" ;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.frag_faxian, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void fetchData() {

    }

    private void initViews(final View view) {
        mCategoryList.clear();
        mCateDetailFragmentList.clear();

        mCategoryList.add(new Category(0, "文章", "", 0));
        mCategoryList.add(new Category(1, "专题", "", 1));
        // 获取子fragment里面cid=0的文章
        BmobRequestUtil.queryArticlesByCategoryID(getActivity(), 0, new ArticleCallbackListener() {
            @Override
            public void onFinish(List<Article> list) {
                if (list != null && list.size() > 0) {
                    // 添加子frag
                    ArticleFragment mArticleFragment = new ArticleFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("cid", mCategoryList.get(0).getId());
                    mBundle.putSerializable("articles", (Serializable) list);
                    mArticleFragment.setArguments(mBundle);
                    mCateDetailFragmentList.add(mArticleFragment);

                    TopicFragment mTopicFragment = new TopicFragment();
                    Bundle mBundle2 = new Bundle();
                    mBundle2.putInt("cid", 1);
                    mCateDetailFragmentList.add(mTopicFragment);

                    mPager = (ViewPager) view.findViewById(R.id.pager_pgMain);
                    mPagerTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_main);
                    mPager.setAdapter(new CategoryFragmentAdaptor(getChildFragmentManager(), mCateDetailFragmentList, mCategoryList));
                    mPagerTabs.setViewPager(mPager);
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }
}
