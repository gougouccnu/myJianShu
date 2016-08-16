package com.ggccnu.myjianshu.mode;

/**
 * Created by lishaowei on 16/8/13.
 */
public class ArticleComment {

    private String comment;

    private String author;

    public ArticleComment(String author, String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
