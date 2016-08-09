package com.ggccnu.myjianshu.ui;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.ggccnu.myjianshu.R;

/**
 * Created by lishaowei on 16/8/6.
 */
public class MyActionProvider extends ActionProvider {

    public MyActionProvider(Context context) {
        super(context);
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add("sub item 1").setIcon(R.drawable.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
        subMenu.add("sub item 2").setIcon(R.drawable.ic_launcher)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
    }
}
