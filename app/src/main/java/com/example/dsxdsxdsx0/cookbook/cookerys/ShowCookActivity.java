package com.example.dsxdsxdsx0.cookbook.cookerys;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.dsxdsxdsx0.cookbook.BaseActivity;
import com.example.dsxdsxdsx0.cookbook.R;
import com.example.dsxdsxdsx0.cookbook.info.ShowCookersInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/8/18.
 *
 * 显示菜谱列表
 *
 */

@ContentView(R.layout.activity_cookery)
public class ShowCookActivity extends BaseActivity {

    private ShowCookersInfo info;
    private ShowCookersInfo tabInfo2;
    private ShowCookersInfo tabInfo3;

    @ViewInject(R.id.linear_bar)
    private LinearLayout mLinearBar;

    @ViewInject(R.id.edit_note_bar)
    private Toolbar mToolbar;

    @ViewInject(R.id.tablayout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.show_detail_viewpager)
    private ViewPager viewPager;

    private List<String> titles;











    @Override
    public void initBefore() {

    }

    @Override
    public void init() {

    }

    @Override
    public void initAfter() {

    }
}
