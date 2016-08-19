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
import com.ggccnu.myjianshu.mode.Comment;
import com.ggccnu.myjianshu.mode.Post;
import com.ggccnu.myjianshu.mode.Reply;
import com.ggccnu.myjianshu.utils.DataAccessUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/8/4.
 */
public class CateDetailActivity extends BaseActivity{

    private static final int QUERY_COMMENT_MSG = 1;
    private static final int QUERY_POST_MEG = 2;
    private ImageView iv_article_author_pic;
    private TextView tv_article_author;
    private TextView tv_article_time;
    private Button btn_guanzhu;
    private WebView wv_article_content;
    private TextView tv_donate_adv;
    private Button btn_donate;

    private RecyclerView rv_post;
    // Bmob原始数据
    private List<Comment>  mCommentList = new ArrayList<Comment>();
    private List<Reply>  mReplyList = new ArrayList<Reply>();

    // 评论列表，数据从Bmob原始数据获得
    private List<ArticleComment> mArticleCommentList = new ArrayList<ArticleComment>();
    private RecyclerView.LayoutManager mLayoutManager;
    // 评论列表适配器
    private ArticleCommentListAdapter mArticleCommentAdapter;

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


        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case QUERY_COMMENT_MSG:
                        mCommentList.addAll((ArrayList<Comment>)msg.obj);
                        // add header null data
                        mArticleCommentList.add(new ArticleComment("header", null, false));
                        // 获取评论列表
                        for (int i = 0; i < mCommentList.size(); i++) {
                            if (mCommentList.get(i).getHasReply()) {
                                final List<ArticleReply> articleReplyList = new ArrayList<>();
                                // 查询到对于的reply list
                                articleReplyList.add(new ArticleReply("author X", "reply"));
                                mArticleCommentList.add(new ArticleComment(mCommentList.get(i).getContent(), articleReplyList, true));
                            } else {
                                mArticleCommentList.add(new ArticleComment(mCommentList.get(i).getContent(), null, false));
                            }
                        }
                        mArticleCommentAdapter = new ArticleCommentListAdapter(mArticleCommentList, CateDetailActivity.this);
                        rv_post.setAdapter(mArticleCommentAdapter);
                        mLayoutManager = new LinearLayoutManager(CateDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        rv_post.setLayoutManager(mLayoutManager);
                        mArticleCommentAdapter.setOnItemClickLitener(new ArticleCommentListAdapter.OnItemClickLitener() {
                            @Override
                            public void onCommentClick(View view, int position) {
                                Toast.makeText(CateDetailActivity.this, "comment " + position + " clicked", Toast.LENGTH_SHORT).show();
                                // 带reply的帖子点击了主帖
                                if (mArticleCommentList.get(position).isHasReply()) {
                                    // reply add到最后
                                    mArticleCommentList.get(position).getArticleReplyList().add(new ArticleReply("author d", "append reply"));
                                    mArticleCommentAdapter.notifyDataSetChanged();
                                } else { // 给不带reply的帖子回复
                                    mArticleCommentList.get(position).setHasReply(true);
                                    List<ArticleReply> newReplyList = new ArrayList<ArticleReply>();
                                    newReplyList.add(new ArticleReply("author c", "reply c"));
                                    // 为comment添加reply
                                    mArticleCommentList.get(position).setArticleReplyList(newReplyList);
                                }
                                mArticleCommentAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onReplyClick(View view, int replyPositon, int commentPosition) {
                                Toast.makeText(CateDetailActivity.this, "comment " + commentPosition + " reply " + replyPositon + " clicked", Toast.LENGTH_SHORT).show();
                                // 添加reply
                                mArticleCommentList.get(commentPosition).getArticleReplyList().add(replyPositon + 1, new ArticleReply("author C", "reply c"));
                                // 刷新列表
                                mArticleCommentAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case QUERY_POST_MEG:

                        break;
                    default:
                        break;
                }
            }
        };

        Post post = new Post();
        post.setObjectId("tCEkMMMh");
        DataAccessUtil.queryComment(this, post, mHandler, QUERY_COMMENT_MSG);

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
