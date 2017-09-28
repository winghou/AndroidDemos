package com.benio.demoproject.hencoder.practice2;

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
public class PracticeDraw2Fragment extends Fragment {

    public static PracticeDraw2Fragment newInstance() {
        PracticeDraw2Fragment fragment = new PracticeDraw2Fragment();
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

        views.add(new Practice01LinearGradientView(context));
        titles.add("LinearGradient");

        views.add(new Practice02RadialGradientView(context));
        titles.add("RadialGradient");

        views.add(new Practice03SweepGradientView(context));
        titles.add("SweepGradient");

        views.add(new Practice04BitmapShaderView(context));
        titles.add("BitmapShader");

        views.add(new Practice05ComposeShaderView(context));
        titles.add("ComposeShader");

        views.add(new Practice06LightingColorFilterView(context));
        titles.add("LightingColorFilter");

        views.add(new Practice07ColorMatrixColorFilterView(context));
        titles.add("ColorMatrixColorFilter");

        views.add(new Practice08XfermodeView(context));
        titles.add("setXfermode()");

        views.add(new Practice09StrokeCapView(context));
        titles.add("setStrokeCap()");

        views.add(new Practice10StrokeJoinView(context));
        titles.add("setStrokeJoin()");

        views.add(new Practice11StrokeMiterView(context));
        titles.add("setStrokeMiter()");

        views.add(new Practice12PathEffectView(context));
        titles.add("setPathEffect()");

        views.add(new Practice13ShadowLayerView(context));
        titles.add("setShadowLayer()");

        views.add(new Practice14MaskFilterView(context));
        titles.add("setMaskFilter()");

        views.add(new Practice15FillPathView(context));
        titles.add("getFillPath()");

        views.add(new Practice16TextPathView(context));
        titles.add("getTextPath()");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
