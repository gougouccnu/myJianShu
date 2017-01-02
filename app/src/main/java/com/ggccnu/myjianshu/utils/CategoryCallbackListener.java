package com.ggccnu.myjianshu.utils;

import com.ggccnu.myjianshu.mode.Category;

import java.util.List;

/**
 * Created by lishaowei on 2017/1/2.
 */
public interface CategoryCallbackListener {

    void onFinish(List<Category> list);
    void onError(String s);
}
