package com.benio.demoproject.hencoder.practice1;

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
 * http://hencoder.com/ui-1-1/
 * HenCoder Android 开发进阶: 自定义 View 1-1 绘制基础
 * Created by zhangzhibin on 2017/9/26.
 */
public class PracticeDraw1Fragment extends Fragment {

    public static PracticeDraw1Fragment newInstance() {
        PracticeDraw1Fragment fragment = new PracticeDraw1Fragment();
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

        views.add(new Practice1DrawColorView(context));
        titles.add("drawColor()");

        views.add(new Practice2DrawCircleView(context));
        titles.add("drawCircle()");

        views.add(new Practice3DrawRectView(context));
        titles.add("drawRect()");

        views.add(new Practice4DrawPointView(context));
        titles.add("drawPoint()");

        views.add(new Practice5DrawOvalView(context));
        titles.add("drawOval()");

        views.add(new Practice6DrawLineView(context));
        titles.add("drawLine()");

        views.add(new Practice7DrawRoundRectView(context));
        titles.add("drawRoundRect()");

        views.add(new Practice8DrawArcView(context));
        titles.add("drawArc()");

        views.add(new Practice9DrawPathView(context));
        titles.add("drawPath()");

        views.add(new Practice10HistogramView(context));
        titles.add("直方图");

        views.add(new Practice11PieChartView(context));
        titles.add("饼图");

        views.add(new Practice12DrawBitmapView(context));
        titles.add("drawBitmap()");

        views.add(new Practice13DrawTextView(context));
        titles.add("drawText()");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
