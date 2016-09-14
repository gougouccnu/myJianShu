package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 16/8/13.
 */
public class Comment extends BmobObject {

    private String commentId;

    private Boolean hasReply;

    private String commenterTimer;

    private String commenterName;

    private String commentContent;

    private String articleObjId;

    private String commenterUrl;

    private String commentPicUrl;

    public String getCommentPicUrl() {
        return commentPicUrl;
    }

    public void setCommentPicUrl(String commentPicUrl) {
        this.commentPicUrl = commentPicUrl;
    }

    public void setArticleObjId(String articleObjId) {
        this.articleObjId = articleObjId;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public void setCommenterTimer(String commenterTimer) {
        this.commenterTimer = commenterTimer;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setHasReply(Boolean hasReply) {
        this.hasReply = hasReply;
    }

    public String getArticleObjId() {
        return articleObjId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public String getCommenterTimer() {
        return commenterTimer;
    }

    public String getCommentId() {
        return commentId;
    }

    public Boolean getHasReply() {
        return hasReply;
    }

    public String getCommenterUrl() {
        return commenterUrl;
    }

    public void setCommenterUrl(String commenterUrl) {
        this.commenterUrl = commenterUrl;
    }
}
