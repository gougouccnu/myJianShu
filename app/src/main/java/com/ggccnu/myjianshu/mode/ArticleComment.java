package com.ggccnu.myjianshu.mode;

import java.util.List;

/**
 * Created by lishaowei on 16/8/12.
 */
public class ArticleComment {

    private String content;

    private Boolean hasReply;

    private List<ArticleReply> articleReplyList;

    public ArticleComment(String comment, List<ArticleReply> articleReplyList, Boolean hasReply) {
        this.content = comment;
        this.articleReplyList = articleReplyList;
        this.hasReply = hasReply;
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
}
