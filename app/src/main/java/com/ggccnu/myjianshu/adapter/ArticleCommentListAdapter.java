package com.ggccnu.myjianshu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.ArticleComment;
import com.ggccnu.myjianshu.mode.ArticleReply;
import com.ggccnu.myjianshu.widget.MyClickText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class ArticleCommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int COMMENT_WITH_REPLY = 1;
    private static final int COMMENT_COMMON = 2;
    private static final int COMMENT_HEADER = 3;
    private List<ArticleComment> mArticleCommentList = new ArrayList<ArticleComment>();
    private Context mContext;

    public  ArticleCommentListAdapter(List<ArticleComment> data, Context context) {
        mArticleCommentList = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == COMMENT_WITH_REPLY) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_reply, parent, false);
            return new ReplyViewHolder(v);
        } else if (viewType == COMMENT_COMMON) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_main, parent, false);
            return new CommonViewHolder(v);
        } else if (viewType == COMMENT_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_comment_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ReplyViewHolder) {
            ((ReplyViewHolder) holder).tv_comment_main.setText(mArticleCommentList.get(position).getContent());
            // 找到reply,添加到comment下面
            List<ArticleReply> articleReplyList = mArticleCommentList.get(position).getArticleReplyList();
            // 新增reply，notify datasetchanged后重绘列表前要remove掉原来的
            ((ReplyViewHolder) holder).parentLayout.removeAllViews();
            for (int i = 0; i < articleReplyList.size(); i++) {
                String replyedAuthor;
                ArticleReply articleReply = articleReplyList.get(i);
                if (i==0) {
                    replyedAuthor = mArticleCommentList.get(position).getAuthor();
                } else {
                    replyedAuthor = articleReplyList.get(i-1).getAuthor();
                }
                ((ReplyViewHolder) holder).parentLayout.addView(addReplyView(replyedAuthor, articleReply.getAuthor(), articleReply.getContent()));
            }
        } else if (holder instanceof CommonViewHolder) {
            ((CommonViewHolder) holder).tv_comment_main.setText(mArticleCommentList.get(position).getContent());
    /*    } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).btn_display_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "display author btn clicked", Toast.LENGTH_SHORT).show();
                }
            });
            ((HeaderViewHolder) holder).btn_sort_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "sort time btn clicked", Toast.LENGTH_SHORT).show();
                }
            });  */
        } else {

        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            if (holder instanceof CommonViewHolder) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onCommentClick(holder.itemView, pos);
                    }
                });
                ((CommonViewHolder) holder).iv_author_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onCommentBtnReplyClick(holder.itemView, pos);
                    }
                });
            }
            // 回复点击事件
            if (holder instanceof ReplyViewHolder) {
                // textview click
                ((ReplyViewHolder) holder).tv_comment_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "replyview main comment clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                ((ReplyViewHolder) holder).img_btn_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickLitener.onReplyQuikClick(holder.itemView, pos);
                    }
                });
                for (int i = 0; i < ((ReplyViewHolder) holder).parentLayout.getChildCount(); i++) {
                    final int finalI = i;
                    ((ReplyViewHolder) holder).parentLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnItemClickLitener.onReplyClick(((ReplyViewHolder) holder).parentLayout, finalI, position);
                        }
                    });
                }
            }
            // header按钮时间回调
            if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).btn_display_author.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onHeaderDisplayAuthorClick();
                    }
                });
                ((HeaderViewHolder) holder).btn_sort_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onHeaderSortTimeClick();
                    }
                });
            }
        }
    }

    private View addReplyView(String replyedAuthor, String author, String content) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_reply, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_reply);
        //tv.setText("@" + author + ":" + content);
        // 帖子作者名称可点击
        SpannableString str = new SpannableString(author + ":" + "@" + replyedAuthor + content);
        str.setSpan(new MyClickText(mContext),0,author.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new MyClickText(mContext),author.length() + 1,author.length() + 2 + replyedAuthor.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //当然这里也可以通过setSpan来设置哪些位置的文本哪些颜色
        tv.setText(str);
        tv.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        tv.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明

        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public int getItemCount() {
        return mArticleCommentList.size();
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_comment_main;
        public ImageButton img_btn_reply;
        public LinearLayout parentLayout;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            tv_comment_main = (TextView) itemView.findViewById(R.id.tv_comment_main);
            img_btn_reply = (ImageButton) itemView.findViewById(R.id.img_btn_reply);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_reply);
        }

    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_comment_main;
        public ImageButton img_btn_reply;

        public RoundedImageView iv_author_pic;

        public CommonViewHolder(View itemView) {
            super(itemView);
            tv_comment_main = (TextView) itemView.findViewById(R.id.tv_comment_main);
            img_btn_reply = (ImageButton) itemView.findViewById(R.id.img_btn_reply);
            iv_author_pic = (RoundedImageView) itemView.findViewById(R.id.iv_author_pic);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_comment_count;
        public Button btn_display_author;
        public Button btn_sort_time;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
            btn_display_author = (Button) itemView.findViewById(R.id.btn_display_author);
            btn_sort_time = (Button) itemView.findViewById(R.id.btn_sort_time);
        }
    }

    public interface OnItemClickLitener
    {
        void onCommentClick(View view, int position);

        void onReplyClick(View view, int replyPosition, int commentPosition);

        void onHeaderDisplayAuthorClick();

        void onHeaderSortTimeClick();

        void onCommentBtnReplyClick(View view, int position);

        void onReplyQuikClick(View itemView, int pos);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p/>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        // listview header
        if (position == 0) {
            return COMMENT_HEADER;
        } else {
            if (mArticleCommentList.get(position).isHasReply()) {
                return COMMENT_WITH_REPLY;
            } else {
                return COMMENT_COMMON;
            }
        }
    }
}
