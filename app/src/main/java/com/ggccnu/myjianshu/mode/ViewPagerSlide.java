package com.ggccnu.myjianshu.mode;

import cn.bmob.v3.BmobObject;

/**
 * Created by lishaowei on 16/7/3.
 */
public class ViewPagerSlide extends BmobObject{

    private Integer id;
    private String pictureUrl;

    public ViewPagerSlide(Integer id, String pictureUrl) {
        this.id = id;
        this.pictureUrl = pictureUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
