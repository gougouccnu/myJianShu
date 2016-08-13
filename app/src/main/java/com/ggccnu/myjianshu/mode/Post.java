package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by lishaowei on 16/8/13.
 */
public class Post extends BmobObject {

    private String title;

    private String content;

    private MyUser author;

    private BmobRelation likes; //多对多关系，用于存储喜欢该帖子的所有用户

    public MyUser getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
