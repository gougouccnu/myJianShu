package com.ggccnu.myjianshu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.MyUser;
import com.ggccnu.myjianshu.mode.Post;
import com.ggccnu.myjianshu.mode.Reply;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/8/17.
 */
public class DataAccessUtil {

    public static void queryPost(final Context context, MyUser user, final Handler mHandler, final int msgWhat) {

        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);
        query.include("author");
        query.findObjects(context, new FindListener<Post>() {
            @Override
            public void onError(int i, String s) {
                Log.d(context.getPackageName(), "queryPost err: " + s);
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

    public static void queryComment(final Context context, Post post, final Handler mHandler, final int msgWhat) {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("post", post);
        query.include("author,post,reply");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onError(int i, String s) {
                Log.d(context.getPackageName(), "queryPost err: " + s);
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

    public static void queryReply(final Context context, Comment comment, final Handler mHandler, final int msgWhat) {

        BmobQuery<Reply> query = new BmobQuery<Reply>();
        query.addWhereEqualTo("comment", comment);
        query.include("author");
        query.findObjects(context, new FindListener<Reply>() {
            @Override
            public void onSuccess(List<Reply> list) {
                Message msg = new Message();
                msg.what = msgWhat;
                msg.obj = list;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(context.getPackageName(), "queryReply err: " + s);
            }
        });
    }
}
