package com.ggccnu.myjianshu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lishaowei on 16/6/19.
 */
public class NoScrolledViewPager extends ViewPager {
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public NoScrolledViewPager(Context context) {
        super(context);
    }

    public NoScrolledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
