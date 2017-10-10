package com.benio.demoproject.hencoder.practice5;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
 * http://hencoder.com/ui-1-5/
 * HenCoder Android 开发进阶：自定义 View 1-5 绘制顺序
 * Created by zhangzhibin on 2017/10/9.
 */
public class PracticeDraw5Fragment extends Fragment {

    public static PracticeDraw5Fragment newInstance() {
        PracticeDraw5Fragment fragment = new PracticeDraw5Fragment();
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

        Practice01AfterOnDrawView view1 = new Practice01AfterOnDrawView(context);
        view1.setImageResource(R.mipmap.batman);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view1.setForeground(new ColorDrawable(Color.parseColor("#88000000")));
        }
        views.add(view1);
        titles.add("AfterOnDraw");

        Practice02BeforeOnDrawView view2 = new Practice02BeforeOnDrawView(context);
        view2.setText(R.string.about_hencoder);
        views.add(view2);
        titles.add("BeforeOnDraw");

        Practice03OnDrawLayout view3 = new Practice03OnDrawLayout(context);
        views.add(view3);
        titles.add("OnDrawLayout");

        Practice04DispatchDrawLayout view4 = new Practice04DispatchDrawLayout(context);
        views.add(view4);
        titles.add("DispatchDraw");

        Practice05AfterOnDrawForegroundView view5 = new Practice05AfterOnDrawForegroundView(context);
        view5.setImageResource(R.mipmap.batman);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view5.setForeground(new ColorDrawable(Color.parseColor("#88000000")));
        }
        views.add(view5);
        titles.add("AfterOnDrawForeground");

        Practice06BeforeOnDrawForegroundView view6 = new Practice06BeforeOnDrawForegroundView(context);
        view6.setImageResource(R.mipmap.batman);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view6.setForeground(new ColorDrawable(Color.parseColor("#88000000")));
        }
        views.add(view6);
        titles.add("BeforeOnDrawForeground");

        Practice07AfterDrawView view7 = new Practice07AfterDrawView(context);
        view7.setImageResource(R.mipmap.batman);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view7.setForeground(new ColorDrawable(Color.parseColor("#88000000")));
        }
        views.add(view7);
        titles.add("AfterDraw");

        Practice08BeforeDrawView view8 = new Practice08BeforeDrawView(context);
        view8.setText(R.string.hello);
        views.add(view8);
        titles.add("BeforeDraw");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
