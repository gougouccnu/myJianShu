package com.ggccnu.myjianshu.utils;

import com.ggccnu.myjianshu.mode.ViewPagerSlide;

import java.util.List;

/**
 * Created by lishaowei on 2017/1/1.
 */
public interface ViewPagerSlideCallbackListener {

    void onFinish(List<ViewPagerSlide> list);
    void onError(String s);
}
