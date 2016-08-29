package com.ggccnu.myjianshu.mode;

import java.util.List;

/**
 * Created by lishaowei on 16/8/12.
 */
public class ArticleComment {

    private String author;

    private String content;

    private Boolean hasReply;

    private String floor_time;

    private String commenterUrl;

    private List<ArticleReply> articleReplyList;

    public ArticleComment(String author, String comment, List<ArticleReply> articleReplyList, Boolean hasReply, String floor_time, String commenterUrl) {
        this.author = author;
        this.content = comment;
        this.articleReplyList = articleReplyList;
        this.hasReply = hasReply;
        this.floor_time = floor_time;
        this.commenterUrl = commenterUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ArticleReply> getArticleReplyList() {
        return articleReplyList;
    }

    public Boolean isHasReply() {
        return hasReply;
    }

    public void setArticleReplyList(List<ArticleReply> articleReplyList) {
        this.articleReplyList = articleReplyList;
    }

    public void setHasReply(boolean hasReply) {
        this.hasReply = hasReply;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFloor_time() {
        return floor_time;
    }

    public void setFloor_time(String floor_time) {
        this.floor_time = floor_time;
    }

    public String getCommenterUrl() {
        return commenterUrl;
    }

    public void setCommenterUrl(String commenterUrl) {
        this.commenterUrl = commenterUrl;
    }
}
