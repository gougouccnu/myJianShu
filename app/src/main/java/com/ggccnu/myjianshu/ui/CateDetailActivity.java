package com.ggccnu.myjianshu.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.ArticleCommentListAdapter;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.ArticleComment;
import com.ggccnu.myjianshu.mode.ArticleReply;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/8/4.
 */
public class CateDetailActivity extends BaseActivity{

    private ImageView iv_article_author_pic;
    private TextView tv_article_author;
    private TextView tv_article_time;
    private Button btn_guanzhu;
    private WebView wv_article_content;
    private TextView tv_donate_adv;
    private Button btn_donate;

    private RecyclerView rv_post;
    // 评论列表，数据从Bmob查询获得
    private List<ArticleComment> mCommentList = new ArrayList<ArticleComment>();
    private RecyclerView.LayoutManager mLayoutManager;
    // 评论列表适配器
    private ArticleCommentListAdapter mCommentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_ui_cate_detail);
        // sets the toolbar as the app bar for the activity
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        final Article articleItem = (Article) getIntent().getSerializableExtra("article_item");

        iv_article_author_pic = (ImageView) findViewById(R.id.iv_author_pic);
        //iv_article_author_pic.setImageURI();
        tv_article_author = (TextView) findViewById(R.id.tv_article_author);
        tv_article_author.setText(articleItem.getAuthor());
        tv_article_time = (TextView) findViewById(R.id.tv_article_time);
        tv_article_time.setText(articleItem.getCreatedAt());
        btn_guanzhu = (Button) findViewById(R.id.btn_guanzhu);
        wv_article_content = (WebView) findViewById(R.id.wv_article_content);
        wv_article_content.getSettings().setJavaScriptEnabled(true);
        wv_article_content.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                        super.doUpdateVisitedHistory(view, url, isReload);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        view.loadUrl("javascript:(function() { " +
                                "document.getElementsByTagName('header')[0].style.display=\"none\"; " +
                                "})()");
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                }

        );
        wv_article_content.loadUrl("http://www.jianshu.com/p/76e27f23e071");
        tv_donate_adv = (TextView) findViewById(R.id.tv_donate_adv);
        btn_donate = (Button) findViewById(R.id.btn_donate);
        rv_post = (RecyclerView) findViewById(R.id.rv_comment);
        // 获取评论列表
        for (int i = 0; i<2; i++) {
            mCommentList.add(new ArticleComment("comment", null, false));
        }
        final List<ArticleReply> articleReplyList = new ArrayList<>();
        articleReplyList.add(new ArticleReply("author X", "reply"));
        mCommentList.add(new ArticleComment("comment", articleReplyList, true));

        mCommentAdapter = new ArticleCommentListAdapter(mCommentList, this);
        rv_post.setAdapter(mCommentAdapter);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_post.setLayoutManager(mLayoutManager);
        mCommentAdapter.setOnItemClickLitener(new ArticleCommentListAdapter.OnItemClickLitener() {
            @Override
            public void onCommentClick(View view, int position) {
                Toast.makeText(CateDetailActivity.this, "comment " + position + " clicked", Toast.LENGTH_SHORT).show();
                // 带reply的帖子点击了主帖
                if (mCommentList.get(position).isHasReply()) {
                    // reply add到最后
                    mCommentList.get(position).getArticleReplyList().add(new ArticleReply("author d", "append reply"));
                    mCommentAdapter.notifyDataSetChanged();
                } else { // 给不带reply的帖子回复
                    mCommentList.get(position).setHasReply(true);
                    List<ArticleReply> newReplyList = new ArrayList<ArticleReply>();
                    newReplyList.add(new ArticleReply("author c", "reply c"));
                    // 为comment添加reply
                    mCommentList.get(position).setArticleReplyList(newReplyList);
                }
                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onReplyClick(View view, int replyPositon, int commentPosition) {
                Toast.makeText(CateDetailActivity.this, "comment " + commentPosition + " reply " + replyPositon + " clicked", Toast.LENGTH_SHORT).show();
                // 添加reply
                mCommentList.get(commentPosition).getArticleReplyList().add(replyPositon + 1, new ArticleReply("author C", "reply c"));
                // 刷新列表
                mCommentAdapter.notifyDataSetChanged();
            }
        });
        //在消息队列中实现对控件的更改
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 31:
                        iv_article_author_pic.setImageBitmap((Bitmap)msg.obj);
                }
            };
        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmpAuthorIcon = getURLimage(articleItem.getAuthorIconUrl());
                Message msg = new Message();
                msg.what = 31;
                msg.obj = bmpAuthorIcon;
                //System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_more:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // TODO:
    //加载图片
    private Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
