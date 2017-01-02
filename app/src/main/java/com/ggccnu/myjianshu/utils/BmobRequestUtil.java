package com.ggccnu.myjianshu.utils;

import android.content.Context;
import android.util.Log;

import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.Reply;
import com.ggccnu.myjianshu.mode.ViewPagerSlide;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/8/17.
 */
public class BmobRequestUtil {

    private static final String TAG = "BmobRequestUtil";
/*
    private E e;
    public BmobRequestUtil(E e) {
        e = e;
    }
*/
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

    public static void queryViewPagerSlideURL(final Context context, final ViewPagerSlideCallbackListener listener) {
        BmobQuery<ViewPagerSlide> slideBmobQuery = new BmobQuery<>();
        slideBmobQuery.order("createdAt");
        slideBmobQuery.findObjects(context, new FindListener<ViewPagerSlide>() {
            @Override
            public void onSuccess(List<ViewPagerSlide> list) {
                listener.onFinish(list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }
        });
    }
/*
    public static <E> void get(final Context context, E e) {

        BmobQuery<e> slideBmobQuery = new BmobQuery<>();
        slideBmobQuery.order("createdAt");
        slideBmobQuery.findObjects(context, new FindListener<e>() {
            @Override
            public void onSuccess(List<e> list) {
                Log.d(TAG, "queryReply success");
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
*/
    public static void queryCategory(final Context context, final CategoryCallbackListener listener) {
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.order("createdAt");// 按照时间降序
        categoryBmobQuery.findObjects(context, new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
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
