package com.ggccnu.myjianshu.utils;

import android.content.Context;
import android.util.Log;

import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.Reply;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/8/17.
 */
public class DataAccessUtil {

    private static final String TAG = "DataAccessUtil";

    public static void queryCommentByArticleObjId(final Context context, String articleObjId, final CommentCallbackListener listener) {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("articleObjId", articleObjId);
        query.include("author,post,reply");
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }

            @Override
            public void onSuccess(List<Comment> list) {
                listener.onFinish(list);}
        });
    }

    public static void queryReplyByCommentId(final Context context, String commentId, final ReplyCallbackListener listener) {

        BmobQuery<Reply> query = new BmobQuery<Reply>();
        query.addWhereEqualTo("commentId", commentId);
        //query.include("author");
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

    public static void queryArticlesByCategoryID(final Context context, Integer categoryID, final ArticleCallbackListener listener) {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.addWhereEqualTo("cid", categoryID);
        articlesBmobQuery.findObjects(context, new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                listener.onFinish(list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }
        });
    }

    public interface ReplyCallbackListener {

        void onFinish(List<Reply> list);
        void onError(String s);
    }

    public interface CommentCallbackListener {

        void onFinish(List<Comment> list);
        void onError(String s);
    }
}
