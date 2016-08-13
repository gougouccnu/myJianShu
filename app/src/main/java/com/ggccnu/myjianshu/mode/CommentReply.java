package com.ggccnu.myjianshu.mode;

/**
 * Created by lishaowei on 16/8/13.
 */
public class CommentReply {

    private String reply;

    private String author;

    public CommentReply(String author, String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
