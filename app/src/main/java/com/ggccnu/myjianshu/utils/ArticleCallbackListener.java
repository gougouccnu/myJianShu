package com.ggccnu.myjianshu.utils;

import com.ggccnu.myjianshu.mode.Article;

import java.util.List;

/**
 * Created by lishaowei on 2017/1/1.
 */
public interface ArticleCallbackListener {

    void onFinish(List<Article> list);
    void onError(String s);
}
