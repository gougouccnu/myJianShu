package com.ggccnu.myjianshu.mode;

/**
 * Created by lishaowei on 16/8/17.
 */
public class ArticleReply {

    private String author;

    private String content;

    public ArticleReply(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
