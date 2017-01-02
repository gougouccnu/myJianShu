package com.ggccnu.myjianshu.utils;

import java.util.List;

/**
 * Created by lishaowei on 2017/1/1.
 */
public interface CallbackListener<T> {

    void onFinish(List<T> list);
    void onError(String s);
}
