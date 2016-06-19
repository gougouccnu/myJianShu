package com.ggccnu.myjianshu.widget;

/**
 * Created by lishaowei on 16/6/18.
 */
public class TabItem {
    /**
     * icon
     */
    public int imageResId;
    /**
     * text
     */
    public int lableResId;

    public Class<? extends BaseFragment> tagFragmentClz;

    public TabItem(int imageResId, int lableResId, Class<? extends BaseFragment> tagFragmentClz) {
        this.imageResId = imageResId;
        this.lableResId = lableResId;
        this.tagFragmentClz = tagFragmentClz;
    }
}
