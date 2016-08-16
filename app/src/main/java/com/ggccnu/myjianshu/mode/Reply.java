package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 16/8/15.
 */
public class Reply extends BmobObject {

    private MyUser author;

    private String content;

    private Comment comment;

    public Reply(MyUser author, String content) {
        this.author = author;
        this.content = content;
    }

    public Reply(String tableName, MyUser author, String content) {
        super(tableName);
        this.author = author;
        this.content = content;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
