package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ggccnu.myjianshu.fragment.CateDetailFragment;
import com.ggccnu.myjianshu.fragment.ChildCateFragment;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Person;
import com.ggccnu.myjianshu.widget.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/6/18.
 */
public class FaxianFragment extends BaseFragment {

    private static final int UPDATE_VIEWPAGE = 0;
    private static final String FAXIAN_FRAGMENT = "FaxianFragment" ;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_faxian, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void fetchData() {

    }

    private void initViews(final View view) {
/*        ArrayList<Category> mCategoryListTemp = (ArrayList<Category>) getIntent().getSerializableExtra("categories");
        if (mCategoryListTemp != null) {
            if (mCategoryListTemp.size() > 0) {
                mCategoryList.addAll(mCategoryListTemp);
                queryArticles();
            } else {
                queryCategory();
            }
        } else {
            queryCategory();
        }
*/
        mCategoryList.add(new Category(0, "文章", "", 0));
        mCategoryList.add(new Category(1, "专题", "", 1));
        queryArticles();
        // 子线程中更新UI
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_VIEWPAGE) {
                    // 添加子frag
                    ChildCateFragment mChildCateFragment = new ChildCateFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("cid", mCategoryList.get(0).getId());
                    mBundle.putSerializable("articles", (Serializable) msg.obj);
                    mChildCateFragment.setArguments(mBundle);
                    mCateDetailFragmentList.add(mChildCateFragment);

                    for (int i = 1; i < mCategoryList.size(); i++) {
                        CateDetailFragment mCateDetailFragment = new CateDetailFragment();
                        Bundle mBundle2 = new Bundle();
                        mBundle2.putInt("cid", mCategoryList.get(i).getId());
                        mBundle2.putSerializable("articles", (Serializable) msg.obj);
                        mCateDetailFragment.setArguments(mBundle2);
                        mCateDetailFragmentList.add(mCateDetailFragment);
                    }
                }

                //ChildCateFragment mChildCateFragment = new ChildCateFragment();
                //mCateDetailFragmentList.add(mChildCateFragment);
                // Initialize the ViewPager and set an adapter
                mPager = (ViewPager) view.findViewById(R.id.pager_pgMain);
                mPagerTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_main);
                mPager.setAdapter(new CategoryFragmentAdaptor(getFragmentManager(), mCateDetailFragmentList, mCategoryList));
                mPagerTabs.setViewPager(mPager);
            }
        };
    }



    private void queryArticles() {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    List<Article> mArticleList = new ArrayList<Article>();
                    for (Article article : list) {
                        mArticleList.add(article);
                    }
                    Message msg = new Message();
                    msg.what = UPDATE_VIEWPAGE;
                    msg.obj = mArticleList;
                    mHandler.sendMessage(msg);
                }
                Log.d(FAXIAN_FRAGMENT, "queryArticles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("MainActivity", "queryArticles onError");
            }
        });
    }

    private void queryCategory() {
        //showLoadingDialog();
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.order("createdAt");// 按照时间降序
        categoryBmobQuery.findObjects(getActivity(), new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
                if (list != null && list.size() > 0) {
                    mCategoryList.addAll(list);
                    queryArticles();
                }
                Log.d(FAXIAN_FRAGMENT, "queryCategory onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(FAXIAN_FRAGMENT, "queryCategory onError");
            }
        });
    }
}
