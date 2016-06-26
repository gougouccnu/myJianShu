package com.ggccnu.myjianshu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ggccnu.myjianshu.R;

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
        View mView;

        listener.onTabClick((TabItem) view.getTag());
        // 设置选中的tab状态为selected
        if (view != selectView) {
            selectView.setSelected(false);
            view.setSelected(true);
            selectView = view;
        }
        for (int i = 0; i < mTabCount; i++) {
            mView = getChildAt(i);
            if (mView == view) {
                ((TabView)mView).mTabLable.setTextColor(0xffff0000);
            } else {
                ((TabView)mView).mTabLable.setTextColor(0xff000000);
            }
        }
        //setTabNotification(3, true);
    }

    /**
     * Sets the text colors for the different states (normal, selected)
     * used for the tabs.
     * @param normalColor
     * @param selectedColor
     */
    public void setTabTextColors(int normalColor, int selectedColor) {
        View view = null;
        for (int i = 0; i < mTabCount; i++) {
            view = getChildAt(i);
            if (view.isSelected()) {
                ((TabView)view).mTabLable.setTextColor(selectedColor);
            } else {
                ((TabView)view).mTabLable.setTextColor(normalColor);
            }
        }
    }

    /**
     * tab栏有新消息时，更改tab的图标
     * @param tabIndex
     * @param hasNotification
     */
    public void setTabNotification(int tabIndex, boolean hasNotification) {
        ImageView imv = ((TabView)getChildAt(tabIndex)).mTabImage;
        if (hasNotification) {
            imv.setImageResource(R.drawable.cb_icon_discover_selected);
        } else {
            imv.setImageResource(R.drawable.cb_icon_discover_normal);
        }
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
