package com.ggccnu.myjianshu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by lishaowei on 16/6/18.
 */
public class TabLayout extends LinearLayout implements OnClickListener {

    private ArrayList<TabItem> tabs;
    private OnTabClickListener listener;
    private int mTabCount;
    private View selectView;

    public TabLayout(Context context) {
        super(context);
        initView();
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        setOrientation(HORIZONTAL);
    }

    public void initData(ArrayList<TabItem>tabs, OnTabClickListener listener){
        this.tabs = tabs;
        this.listener = listener;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        if(tabs != null && tabs.size() > 0){
            mTabCount = tabs.size();
            TabView mTabView = null;
            for (int i = 0;i < tabs.size(); i++) {
                mTabView = new TabView(getContext());
                mTabView.setTag(tabs.get(i));
                mTabView.initData(tabs.get(i));
                mTabView.setOnClickListener(this);
                addView(mTabView, params);
            }
        } else {
            throw new IllegalArgumentException("tabs can not be empty");
        }
    }

    @Override
    public void onClick(View view) {
        listener.onTabClick((TabItem) view.getTag());
    }

    public void setCurrentTab(int i) {
        View view = null;
        if (i < mTabCount && i >= 0) {
            view = getChildAt(i);
        }
        if (selectView != view) {
            view.setSelected(true);
            if (selectView != null) {
                selectView.setSelected(false);
            }
            selectView = view;
        }
    }

    public interface OnTabClickListener{

        void onTabClick(TabItem tabItem);
    }
}
