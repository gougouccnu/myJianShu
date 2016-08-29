package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 16/8/15.
 */
public class Reply extends BmobObject {

    private String replyContent;

    private String replyWho;

    private String replyAuthor;

    private String commentId;

    public Reply(String commentId, String replyAuthor, String replyContent, String replyWho) {
        this.commentId = commentId;
        this.replyAuthor = replyAuthor;
        this.replyContent = replyContent;
        this.replyWho = replyWho;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setReplyAuthor(String replyAuthor) {
        this.replyAuthor = replyAuthor;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setReplyWho(String replyWho) {
        this.replyWho = replyWho;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getReplyAuthor() {
        return replyAuthor;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public String getReplyWho() {
        return replyWho;
    }
}
