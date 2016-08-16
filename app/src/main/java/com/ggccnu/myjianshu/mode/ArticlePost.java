package com.ggccnu.myjianshu.mode;

import java.util.List;

/**
 * Created by lishaowei on 16/8/12.
 */
public class ArticlePost {

    private String post;

    private Boolean hasReply;

    private List<ArticleComment> articleCommentList;

    public ArticlePost(String comment, List<ArticleComment> articleCommentList, boolean hasReply) {
        this.post = comment;
        this.articleCommentList = articleCommentList;
        this.hasReply = hasReply;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<ArticleComment> getArticleCommentList() {
        return articleCommentList;
    }

    public boolean isHasReply() {
        return hasReply;
    }

    public void setArticleCommentList(List<ArticleComment> articleCommentList) {
        this.articleCommentList = articleCommentList;
    }

    public void setHasReply(boolean hasReply) {
        this.hasReply = hasReply;
    }
}
