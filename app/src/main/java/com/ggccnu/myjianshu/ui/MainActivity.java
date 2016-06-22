package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.view.Window;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.widget.BaseFragment;
import com.ggccnu.myjianshu.widget.TabItem;
import com.ggccnu.myjianshu.widget.TabLayout;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements TabLayout.OnTabClickListener{


    private TabLayout mTabLayout;
    ArrayList<TabItem> tabs;
    private BaseFragment fragment;
    //private NoScrolledViewPager mNoScrolledViewPager;


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
        //mNoScrolledViewPager = (NoScrolledViewPager) findViewById(R.id.no_scrolled_viewpager);
    }

    private void initData(){
        tabs = new ArrayList<TabItem>();
        tabs.add(new TabItem(R.drawable.selector_icon_faxian, R.string.faxian, FaxianFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.guanzhu, GuanzhuFragment.class));
        tabs.add(new TabItem(R.drawable.cb_icon_pen_normal, R.string.write, GuanzhuFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_xiaoxi, R.string.xiaoxi, XiaoxiFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_wode, R.string.wode, WodeFragment.class));

        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);
    }

    @Override
    public void onTabClick(TabItem tabItem) {
        try {
            BaseFragment fragment= tabItem.tagFragmentClz.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commitAllowingStateLoss();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
