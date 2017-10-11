package com.benio.demoproject.hencoder.practice6;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benio.demoproject.R;
import com.benio.demoproject.hencoder.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * http://hencoder.com/ui-1-6/
 * HenCoder Android 自定义 View 1-6：属性动画 Property Animation（上手篇）
 * Created by zhangzhibin on 2017/10/10.
 */
public class PracticeDraw6Fragment extends Fragment {

    public static PracticeDraw6Fragment newInstance() {
        PracticeDraw6Fragment fragment = new PracticeDraw6Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_practice_draw1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Context context = getContext();
        List<View> views = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        views.add(new Practice01Translation(context));
        titles.add("translation");

        views.add(new Practice02Rotation(context));
        titles.add("rotation");

        views.add(new Practice03Scale(context));
        titles.add("scale");

        views.add(new Practice04Alpha(context));
        titles.add("alpha");

        views.add(new Practice05MultiProperties(context));
        titles.add("multiProperties");

        views.add(new Practice06Duration(context));
        titles.add("duration");

        views.add(new Practice07Interpolator(context));
        titles.add("interpolator");

        views.add(new Practice08ObjectAnimatorLayout(context));
        titles.add("objectAnimator");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
