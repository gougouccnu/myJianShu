package com.ggccnu.myjianshu.ui;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyAppalication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
