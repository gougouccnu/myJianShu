package com.ggccnu.myjianshu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.mode.ArticleComment;
import com.ggccnu.myjianshu.mode.CommentReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int COMMENT_WITH_REPLY = 1;
    private static final int COMMENT_COMMON = 2;
    private List<ArticleComment> mCommentList = new ArrayList<ArticleComment>();
    private Context mContext;

    public CommentListAdapter(List<ArticleComment> data, Context context) {
        mCommentList = data;
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
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ReplyViewHolder) {
            ((ReplyViewHolder) holder).tv_comment_main.setText("common");
            // 找到reply,添加到comment下面
            List<CommentReply> commentReplyList = mCommentList.get(position).getCommentReplyList();
            // 新增reply，notify datasetchanged后重绘列表前要remove掉原来的
            ((ReplyViewHolder) holder).parentLayout.removeAllViews();
            for (int i = 0; i < commentReplyList.size(); i++) {
                CommentReply commentReply = commentReplyList.get(i);
                ((ReplyViewHolder) holder).parentLayout.addView(addReplyView(commentReply.getAuthor(), commentReply.getReply()));
            }
        } else if (holder instanceof CommonViewHolder) {
            ((CommonViewHolder) holder).tv_comment_main.setText("common");
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onCommentClick(holder.itemView, pos);
                }
            });
            // 回复点击事件
            if (holder instanceof ReplyViewHolder) {
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
        }
    }

    private View addReplyView(String author, String content) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_reply, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_reply);
        tv.setText("@" + "author A " + content);
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_comment_main;
        public LinearLayout parentLayout;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            tv_comment_main = (TextView) itemView.findViewById(R.id.tv_comment_main);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_reply);
        }

    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_comment_main;

        public CommonViewHolder(View itemView) {
            super(itemView);
            tv_comment_main = (TextView) itemView.findViewById(R.id.tv_comment_main);
        }
    }

    public interface OnItemClickLitener
    {
        void onCommentClick(View view, int position);
        void onReplyClick(View view, int replyPosition, int commentPosition);
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
        if (mCommentList.get(position).isHasReply()) {
            return COMMENT_WITH_REPLY;
        } else {
            return COMMENT_COMMON;
        }
    }
    public void updateCommentList(ArrayList<ArticleComment> commentArrayList) {
        mCommentList.clear();
        mCommentList.addAll(commentArrayList);
        notifyDataSetChanged();
    }
}
