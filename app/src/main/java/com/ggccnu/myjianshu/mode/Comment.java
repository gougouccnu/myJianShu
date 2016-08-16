package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 16/8/13.
 */
public class Comment extends BmobObject {

    private String content;

    private MyUser author;//评论的用户

    private Post post; //所评论的帖子，这里体现的是一对多的关系，一个评论属于一个帖子

    private Boolean hasReply;

    public String getContent() {
        return content;
    }

    public Post getPost() {
        return post;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public Boolean getHasReply() {
        return hasReply;
    }

    public void setHasReply(Boolean hasReply) {
        this.hasReply = hasReply;
    }
}
