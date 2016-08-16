package com.ggccnu.myjianshu.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.MyUser;
import com.ggccnu.myjianshu.mode.Post;
import com.ggccnu.myjianshu.mode.Reply;

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

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_splash);

        Bmob.initialize(this, "ca3f68beaf133e348d60dcbd5f63d20c");
        BmobUpdateAgent.update(this);

        // mode test
        MyUser currentUsr = new MyUser();
        currentUsr.setObjectId("misb0004");
        queryPost(currentUsr);

        Post post = new Post();
        post.setObjectId("tCEkMMMh");
        queryComment(post);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == QUERY_COMMENT_SUCCESS) {
                    Comment comment = new Comment();
                    comment.setObjectId("Tvl1IIIJ");
                    queryReply(((List<Comment>) msg.obj).get(1));
                }
            }
        };
//


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

    private List<Post> queryPost(MyUser user) {

        final List<Post> returnPostList = new ArrayList<Post>();

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
                returnPostList.addAll(list);
            }
        });
        return returnPostList;
    }

    private List<Comment> queryComment(Post post) {

        final List<Comment> returnCommentList = new ArrayList<Comment>();

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
                returnCommentList.addAll(list);
                Message msg = new Message();
                msg.what = QUERY_COMMENT_SUCCESS;
                msg.obj = returnCommentList;
                mHandler.sendMessage(msg);
            }
        });
        return returnCommentList;
    }

    private List<Reply> queryReply(Comment comment) {

        final List<Reply> returnReplyList = new ArrayList<Reply>();

        BmobQuery<Reply> query = new BmobQuery<Reply>();
        query.addWhereEqualTo("comment", comment);
        query.include("author");
        query.findObjects(this, new FindListener<Reply>() {
            @Override
            public void onSuccess(List<Reply> list) {
                returnReplyList.addAll(list);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryReply err: " + s);
            }
        });
        return returnReplyList;
    }
}
