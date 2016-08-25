package com.example.dsxdsxdsx0.cookbook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dsxdsxdsx0.cookbook.cookerys.ShowCookFragment;

import java.util.List;

/**
 * Created by dsxdsxdsx0 on 2016/8/25.
 *
 * FragmentPagerAdapter : FragmentPagerAdapter 被用来处理多 Fragment 页面的横向滑动,
 * 该类内的每一个生成的 Fragment 都将保存在内存之中，
 * 因此适用于那些相对静态的页，数量也比较少的那种；如果需要处理有很多页，
 * 并且数据动态性较大、占用内存较多的情况，应该使用FragmentStatePagerAdapter。
 *
 */
public class ShowCookAdapter extends FragmentPagerAdapter {

    private List<ShowCookFragment> fragmentList;

    private List<String> title;

    public ShowCookAdapter(FragmentManager fm, List<ShowCookFragment> fragmentList, List<String> title) {
        super(fm);
        this.fragmentList = fragmentList;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
