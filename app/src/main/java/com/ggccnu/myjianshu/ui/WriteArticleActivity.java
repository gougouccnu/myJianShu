package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.view.Window;

import com.ggccnu.myjianshu.R;

/**
 * Created by lishaowei on 16/6/27.
 */
public class WriteArticleActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_write_article);
    }
}
