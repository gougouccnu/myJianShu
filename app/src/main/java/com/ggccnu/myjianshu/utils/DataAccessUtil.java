package com.ggccnu.myjianshu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.MyUser;
import com.ggccnu.myjianshu.mode.Post;
import com.ggccnu.myjianshu.mode.Reply;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/8/17.
 */
public class DataAccessUtil {

    private static final String TAG = "DataAccessUtil";

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

        final List<Comment> mCommentList = new ArrayList<Comment>();
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("post", post);
        query.include("author,post,reply");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryPost err: " + s);
            }

            @Override
            public void onSuccess(List<Comment> list) {
                mCommentList.addAll(list);
                Message msg = new Message();
                msg.what = msgWhat;
                msg.obj = mCommentList;
                mHandler.sendMessage(msg);
            }
        });
    }

    public static void queryReply(final Context context, Comment comment, final QueryReplyCallbackListener listener) {

        BmobQuery<Reply> query = new BmobQuery<Reply>();
        query.addWhereEqualTo("comment", comment);
        query.include("author");
        query.findObjects(context, new FindListener<Reply>() {
            @Override
            public void onSuccess(List<Reply> list) {
                Log.d(TAG, "queryReply success");
                listener.onFinish(list);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryReply err: " + s);
                listener.onError(s);
            }
        });
    }

    public interface QueryReplyCallbackListener {

        void onFinish(List<Reply> list);
        void onError(String s);
    }
}
