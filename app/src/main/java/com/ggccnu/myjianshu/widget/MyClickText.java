package com.ggccnu.myjianshu.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

/**
 * Created by lishaowei on 16/8/19.
 */
public class MyClickText extends ClickableSpan {

    private Context context;

    public MyClickText(Context context) {
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        //设置文本的颜色
        ds.setColor(Color.BLUE);
        //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        Toast.makeText(context,"发生了点击效果",Toast.LENGTH_SHORT).show();
    }
}
