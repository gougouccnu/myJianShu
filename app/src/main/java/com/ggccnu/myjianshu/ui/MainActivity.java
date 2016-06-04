package com.ggccnu.myjianshu.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ui_main);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager_pgMain);
        pager.setAdapter(new CategoryFragmentAdaptor(getSupportFragmentManager(),));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_main);
        tabs.setViewPager(pager);
    }
}
