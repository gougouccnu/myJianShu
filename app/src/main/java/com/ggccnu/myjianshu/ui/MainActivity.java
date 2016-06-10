package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;
import com.ggccnu.myjianshu.fragment.CateDetailFragment;
import com.ggccnu.myjianshu.fragment.ChildCateFragment;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_VIEWPAGE = 0;
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
    private ArrayList<android.support.v4.app.Fragment> mCateDetailFragmentList   = new ArrayList<>();

    /**
     * 分类数组
     */
    private ArrayList<Category> mCategoryList = new ArrayList<>();

    private Handler mHandler;
    private Person mPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_main);

        //initBmobData();
        initView();
    }

    private void initBmobData() {
        for (int i = 1; i < 5; i++) {
            for (int articleId = 0; articleId < 5; articleId++) {
                new Article(articleId, "Lucy", i, 2, 5, 1, 3, "日报", "404", "404", "biaoti").save(this, new SaveListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
            new Category(i, "Tab" + i, " ", 1).save(this, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    private void initView() {
        ArrayList<Category> mCategoryListTemp = (ArrayList<Category>) getIntent().getSerializableExtra("categories");
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
                mPager = (ViewPager) findViewById(R.id.pager_pgMain);
                mPagerTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_main);
                mPager.setAdapter(new CategoryFragmentAdaptor(getSupportFragmentManager(), mCateDetailFragmentList, mCategoryList));
                mPagerTabs.setViewPager(mPager);
            }
        };

    }

    /**
     * 查询文章
     */
    private void queryArticles() {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.findObjects(this, new FindListener<Article>() {
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
                Log.d("MainActivity", "queryArticles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("MainActivity", "queryArticles onError");
            }
        });
    }

    /**
     * 查询分类
     */
    private void queryCategory() {
        //showLoadingDialog();
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.order("createdAt");// 按照时间降序
        categoryBmobQuery.findObjects(this, new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
                if (list != null && list.size() > 0) {
                    mCategoryList.addAll(list);
                    queryArticles();
                }
                Log.d("MainActivity", "queryCategory onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("MainActivity", "queryCategory onError");
            }
        });
    }
}
