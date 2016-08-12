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

import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int COMMENT_WITH_REPLY = 1;
    private static final int COMMENT_COMMON = 2;
    private List<ArticleComment> mCommentList;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReplyViewHolder) {
            ((ReplyViewHolder) holder).tv_comment_main.setText("common");
            TextView tv_reply = new TextView(mContext);
            tv_reply.setText("reply");

            ((ReplyViewHolder) holder).parentLayout.addView(tv_reply, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        } else if (holder instanceof CommonViewHolder) {
            ((CommonViewHolder) holder).tv_comment_main.setText("common");
        }
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
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
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
        if (position == 0) {
            return COMMENT_COMMON;
        } else {
            return COMMENT_WITH_REPLY;
        }
    }
}
