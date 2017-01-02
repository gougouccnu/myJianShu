package com.ggccnu.myjianshu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.utils.CategoryCallbackListener;
import com.ggccnu.myjianshu.utils.BmobRequestUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by lishaowei on 16/6/4.
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";
    private static final int QUERY_COMMENT_SUCCESS = 1;
    private static final int QUERY_REPLY_SUCCESS = 2;
    private static final int QUERY_POST_MEG = 3;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_splash);

        Bmob.initialize(this, "ca3f68beaf133e348d60dcbd5f63d20c");
        BmobUpdateAgent.update(this);

        BmobRequestUtil.queryCategory(this, new CategoryCallbackListener() {
            @Override
            public void onFinish(List<Category> list) {
                if (list != null && list.size() > 0) {
                    ArrayList<Category> categories = new ArrayList<Category>();
                    categories.addAll(list);
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("categories", (Serializable) categories);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(String s) {

            }
        });
/*
        BmobRequestUtil<Category> dataAccessUtil = new BmobRequestUtil<Category>();
        dataAccessUtil.get(this, new Category());
*/
    }
}
