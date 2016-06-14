package com.ggccnu.myjianshu.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.ggccnu.myjianshu.mode.Person;

/**
 * Created by lishaowei on 16/6/4.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * 界面底部工具栏处理
     */


    /**
     * 界面顶部工具栏处理
     */

    /**
     * 用户是否登录处理
     */

    public Person getCurrentPerson(Context context) {
        Person person = Person.getCurrentUser(context, Person.class);
        return  person;
    }
}
