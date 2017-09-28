package com.benio.demoproject.hencoder;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhangzhibin on 2017/9/27.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;
    private List<String> mTitles;

    public ViewPagerAdapter(List<View> views, List<String> titles) {
        mViews = views;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
