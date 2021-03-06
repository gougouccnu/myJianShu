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
    private int mCurrentTabIndex;
    private ArrayList<BaseFragment> tabsFragmetList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedStateMap = new HashMap<String, Fragment.SavedState>();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_main);

        initView();
        initData();
    }

    public void setFragmentSavedState(String key, Fragment.SavedState state){
        savedStateMap.put(key, state);
    }

    public Fragment.SavedState getFragmentSavedState(String key){
        return savedStateMap.get(key);
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        imgBtnWrite = (ImageButton) findViewById(R.id.img_button_write);
        imgBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WriteArticleActivity.class));
            }
        });
    }

    private void initData(){
        tabs = new ArrayList<TabItem>();
        tabs.add(new TabItem(R.drawable.selector_icon_faxian, R.string.faxian));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.guanzhu));
        tabs.add(new TabItem(R.drawable.selector_icon_guanzhu, R.string.write));
        tabs.add(new TabItem(R.drawable.selector_icon_xiaoxi, R.string.xiaoxi));
        tabs.add(new TabItem(R.drawable.selector_icon_wode, R.string.wode));

        tabsFragmetList = new ArrayList<BaseFragment>();
        tabsFragmetList.add(new FaxianFragment());
        tabsFragmetList.add(new GuanzhuFragment());
        tabsFragmetList.add(new GuanzhuFragment()); //fake fragment
        tabsFragmetList.add(new XiaoxiFragment());
        tabsFragmetList.add(new WodeFragment());

        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment, tabsFragmetList.get(0)).commit();
        mCurrentTabIndex = 0;
    }

    @Override
    public void onTabClick(TabItem tabItem) {
        int toTabIndex = tabs.indexOf(tabItem);
        if(toTabIndex != mCurrentTabIndex) {
            if(tabsFragmetList.get(toTabIndex).isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .show(tabsFragmetList.get(toTabIndex))
                        .hide(tabsFragmetList.get(mCurrentTabIndex))
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment, tabsFragmetList.get(toTabIndex))
                        .hide(tabsFragmetList.get(mCurrentTabIndex))
                        .commit();
            }
            mCurrentTabIndex = toTabIndex;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
