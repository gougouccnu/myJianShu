package com.ggccnu.myjianshu.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.Article;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> mArticleList;

    public ArticleAdapter(List<Article> data) {
        mArticleList = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_article, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Uri mUriAuthorPic = null;
        Uri mUriArticlePicture = null;

        //在消息队列中实现对控件的更改
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10:
                        //System.out.println("111");
                        Bitmap bmp=(Bitmap)msg.obj;
                        holder.iv_article_picture.setImageBitmap(bmp);
                        break;
                }
            };
        };

        //holder.iv_author_pic.setImageURI(mUriAuthorPic);
        holder.tv_author_name.setText(mArticleList.get(position).getAuthor());
        holder.tv_article_time.setText(mArticleList.get(position).getCreatedAt());
        holder.tv_article_title.setText(mArticleList.get(position).getTitle());
        holder.tv_article_tag.setText(mArticleList.get(position).getTag());
        holder.tv_article_likes.setText(mArticleList.get(position).getReadTimes() + "次阅."
                + mArticleList.get(position).getComments() + "评论." + mArticleList.get(position).getLikes()
                + "喜欢");
        //holder.iv_article_picture.setImageURI(mUriArticlePicture);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(mArticleList.get(position).getPictureUrl());
                Message msg = new Message();
                msg.what = 10;
                msg.obj = bmp;
                //System.out.println("000");
                handle.sendMessage(msg);
            }
        }).start();


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_author_pic;
        public TextView tv_author_name;
        public TextView tv_article_time;
        public TextView tv_article_title;
        public TextView tv_article_tag;
        public TextView tv_article_likes;
        public ImageView iv_article_picture;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_author_pic = (ImageView) itemView.findViewById(R.id.iv_author_pic);
            tv_author_name = (TextView) itemView.findViewById(R.id.tv_author_name);
            tv_article_time = (TextView) itemView.findViewById(R.id.tv_article_time);
            tv_article_title = (TextView) itemView.findViewById(R.id.tv_article_title);
            tv_article_tag = (TextView) itemView.findViewById(R.id.tv_article_category);
            tv_article_likes = (TextView) itemView.findViewById(R.id.tv_article_likes);
            iv_article_picture = (ImageView) itemView.findViewById(R.id.iv_article_picture);
        }

    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
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
