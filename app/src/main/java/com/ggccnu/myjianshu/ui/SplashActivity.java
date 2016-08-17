package com.ggccnu.myjianshu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.MyUser;
import com.ggccnu.myjianshu.mode.Post;
import com.ggccnu.myjianshu.utils.DataAccessUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
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

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case QUERY_COMMENT_SUCCESS:
                        Comment comment = new Comment();
                        comment.setObjectId("Tvl1IIIJ");
                        DataAccessUtil.queryReply(SplashActivity.this, ((List<Comment>) msg.obj).get(1), mHandler,QUERY_REPLY_SUCCESS);
                        break;
                    case QUERY_POST_MEG:

                        break;
                    default:
                        break;
                }
            }
        };

        // mode test
        MyUser currentUsr = new MyUser();
        currentUsr.setObjectId("misb0004");
        DataAccessUtil.queryPost(this, currentUsr, mHandler,QUERY_POST_MEG);

        Post post = new Post();
        post.setObjectId("tCEkMMMh");
        DataAccessUtil.queryComment(this, post, mHandler, QUERY_COMMENT_SUCCESS);
        
        queryCategory();

    }

    private void queryCategory() {
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.order("createdAt");// 按照时间降序
        categoryBmobQuery.findObjects(this, new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
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
            public void onError(int i, String s) {
                //HankkinUtils.showToast(SplasActivity.this, s);
            }
        });
    }
/*
    private void queryPost(MyUser user, final Handler mHandler, final int msgWhat) {

        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);
        query.include("author");
        query.findObjects(this, new FindListener<Post>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryPost err: " + s);
            }

            @Override
            public void onSuccess(List<Post> list) {
                Message msg = new Message();
                msg.what = msgWhat;
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void queryComment(Post post, final Handler mHandler, final int msgWhat) {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("post", post);
        query.include("author,post,reply");
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryPost err: " + s);
            }

            @Override
            public void onSuccess(List<Comment> list) {
                Message msg = new Message();
                msg.what = msgWhat;
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void queryReply(Comment comment, final Handler mHandler, final int msgWhat) {

        BmobQuery<Reply> query = new BmobQuery<Reply>();
        query.addWhereEqualTo("comment", comment);
        query.include("author");
        query.findObjects(this, new FindListener<Reply>() {
            @Override
            public void onSuccess(List<Reply> list) {
                Message msg = new Message();
                msg.what = msgWhat;
                msg.obj = list;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryReply err: " + s);
            }
        });
    }

    */
}
