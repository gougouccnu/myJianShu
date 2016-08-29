package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 16/6/4.
 */
public class Article extends BmobObject {

    private int id;
    private String author;
    private int cid;
    private int readTimes;
    private int comments;
    private int likes;
    private int donate;
    private String tag;
    private String authorIconUrl;
    private String pictureUrl;
    private String title;
    private String articleUrl;
    private String articleObjId;

    public Article(String articleObjId, String articleUrl, String author, String authorIconUrl, int cid, int comments, int donate, int id, int likes, String pictureUrl, int readTimes, String tag, String title) {
        this.articleObjId = articleObjId;
        this.articleUrl = articleUrl;
        this.author = author;
        this.authorIconUrl = authorIconUrl;
        this.cid = cid;
        this.comments = comments;
        this.donate = donate;
        this.id = id;
        this.likes = likes;
        this.pictureUrl = pictureUrl;
        this.readTimes = readTimes;
        this.tag = tag;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public int getCid() {
        return cid;
    }

    public int getReadTimes() {
        return readTimes;
    }

    public int getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public int getDonate() {
        return donate;
    }

    public String getTag() {
        return tag;
    }

    public String getAuthorIconUrl() {
        return authorIconUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setReadTimes(int readTimes) {
        this.readTimes = readTimes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDonate(int donate) {
        this.donate = donate;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setAuthorIconUrl(String authorIconUrl) {
        this.authorIconUrl = authorIconUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleObjId() {
        return articleObjId;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleObjId(String articleObjId) {
        this.articleObjId = articleObjId;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
