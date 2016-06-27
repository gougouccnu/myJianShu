package com.ggccnu.myjianshu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.widget.BaseFragment;
import com.ggccnu.myjianshu.widget.TabItem;
import com.ggccnu.myjianshu.widget.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements TabLayout.OnTabClickListener{


    private TabLayout mTabLayout;
    ArrayList<TabItem> tabs;
    private BaseFragment fragment;
    //private NoScrolledViewPager mNoScrolledViewPager;
    Map<String, Fragment.SavedState> savedStateMap;
    private ImageButton imgBtnWrite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedStateMap = new HashMap<String, Fragment.SavedState>();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_main);

        //initBmobData();
        initView2();
        initData();
        imgBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WriteArticleActivity.class));
            }
        });
    }

    public void setFragmentSavedState(String key, Fragment.SavedState state){
        savedStateMap.put(key, state);
    }

    public Fragment.SavedState getFragmentSavedState(String key){
        return savedStateMap.get(key);
    }

    private void initView2() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        imgBtnWrite = (ImageButton) findViewById(R.id.img_button_write);
        //mNoScrolledViewPager = (NoScrolledViewPager) findViewById(R.id.no_scrolled_viewpager);
    }

    private void initData(){
        tabs = new ArrayList<TabItem>();
        tabs.add(new TabItem(R.drawable.selector_icon_faxian, R.string.faxian, FaxianFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.guanzhu, GuanzhuFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.write, GuanzhuFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_xiaoxi, R.string.xiaoxi, XiaoxiFragment.class));
        tabs.add(new TabItem(R.drawable.selector_icon_wode, R.string.wode, WodeFragment.class));

        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);
        try {
            BaseFragment fragment = tabs.get(0).tagFragmentClz.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commitAllowingStateLoss();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTabClick(TabItem tabItem) {
        try {
            BaseFragment fragment= tabItem.tagFragmentClz.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commitAllowingStateLoss();
            //getChildFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //((NestedFragApp) getActivity().getApplication()).setFragmentSavedState(
        //        SAVED_STATE_KEY, getFragmentManager().saveFragmentInstanceState(this));
    }
}
