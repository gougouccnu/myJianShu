package com.ggccnu.myjianshu.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.Article;

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
    public void onBindViewHolder(ViewHolder holder, int position) {

        Uri mUriAuthorPic = null;
        Uri mUriArticlePicture = null;

        //holder.iv_author_pic.setImageURI(mUriAuthorPic);
        holder.tv_author_name.setText(mArticleList.get(position).getAuthor());
        holder.tv_article_time.setText(mArticleList.get(position).getCreatedAt());
        holder.tv_article_title.setText(mArticleList.get(position).getTitle());
        holder.tv_article_tag.setText(mArticleList.get(position).getTag());
        holder.tv_article_likes.setText(mArticleList.get(position).getReadTimes() + "次阅."
                + mArticleList.get(position).getComments() + "评论." + mArticleList.get(position).getLikes()
                + "喜欢");
        //holder.iv_article_picture.setImageURI(mUriArticlePicture);
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        @Override
        public void onClick(View view) {

        }
    }

    // TODO:

}
