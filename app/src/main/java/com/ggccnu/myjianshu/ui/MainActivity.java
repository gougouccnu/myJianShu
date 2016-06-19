package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.widget.BaseFragment;
import com.ggccnu.myjianshu.widget.TabItem;
import com.ggccnu.myjianshu.widget.TabLayout;

import java.util.ArrayList;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements TabLayout.OnTabClickListener{


    private TabLayout mTabLayout;
    ArrayList<TabItem> tabs;
    private BaseFragment fragment;

    //@Bind(R.id.btn_FaXian)
    //Button btnFaxian;
    @Bind(R.id.btn_GuanZhu)
    Button btnGuanZhu;
    @Bind(R.id.iv_Write)
    ImageView ivWrite;
    @Bind(R.id.btn_XiaoXi)
    Button btnXiaoxi;
    @Bind(R.id.btn_WoDe)
    Button btnWoDe;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_main);

        //initBmobData();
        initView2();
        initData();
    }

    private void initView2() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initData(){
        tabs = new ArrayList<TabItem>();
        tabs.add(new TabItem(R.drawable.selector_icon_faxian, R.string.faxian, FaxianFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.guanzhu, GuanzhuFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.guanzhu, GuanzhuFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_xiaoxi, R.string.xiaoxi, XiaoxiFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_wode, R.string.wode, WodeFragment.class));
        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);

        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onTabClick(TabItem tabItem) {
        try {
            BaseFragment fragment = tabItem.tagFragmentClz.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mViewPager.setCurrentItem(tabs.indexOf(tabItem));
    }
/*
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

        Button btnFaxian = (Button) findViewById(R.id.btn_FaXian);
        if (btnFaxian != null) {
            btnFaxian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }




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

    */

    public class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            try {
                return tabs.get(position).tagFragmentClz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return tabs.size();
        }
    }
}
