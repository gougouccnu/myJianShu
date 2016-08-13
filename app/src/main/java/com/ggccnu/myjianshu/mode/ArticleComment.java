package com.ggccnu.myjianshu.mode;

import java.util.List;

/**
 * Created by lishaowei on 16/8/12.
 */
public class ArticleComment {

    public String comment;

    public boolean hasReply;

    public List<CommentReply>  commentReplyList;

    public ArticleComment(String comment, List<CommentReply> commentReplyList, boolean hasReply) {
        this.comment = comment;
        this.commentReplyList = commentReplyList;
        this.hasReply = hasReply;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CommentReply> getCommentReplyList() {
        return commentReplyList;
    }

    public boolean isHasReply() {
        return hasReply;
    }

    public void setCommentReplyList(List<CommentReply> commentReplyList) {
        this.commentReplyList = commentReplyList;
    }

    public void setHasReply(boolean hasReply) {
        this.hasReply = hasReply;
    }
}
