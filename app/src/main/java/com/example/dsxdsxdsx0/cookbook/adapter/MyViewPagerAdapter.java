package com.example.dsxdsxdsx0.cookbook.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by dsxdsxdsx0 on 2016/8/15.
 */
public class MyViewPagerAdapter extends PagerAdapter {

    ImageView []imageViews;

    public MyViewPagerAdapter(ImageView[] imageViews) {
        this.imageViews = imageViews;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //删除view


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews[position % imageViews.length]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews[position % imageViews.length],0);
        return imageViews[position % imageViews.length];
    }
}
