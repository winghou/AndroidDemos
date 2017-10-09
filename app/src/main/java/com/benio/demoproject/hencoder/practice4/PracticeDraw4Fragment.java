package com.benio.demoproject.hencoder.practice4;

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
 * http://hencoder.com/ui-1-4/
 * HenCoder Android 开发进阶：自定义 View 1-4 Canvas 对绘制的辅助 clipXXX() 和 Matrix
 * Created by zhangzhibin on 2017/9/29.
 */
public class PracticeDraw4Fragment extends Fragment {

    public static PracticeDraw4Fragment newInstance() {
        PracticeDraw4Fragment fragment = new PracticeDraw4Fragment();
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

        views.add(new Practice01ClipRectView(context));
        titles.add("clipRect()");

        views.add(new Practice02ClipPathView(context));
        titles.add("clipPath()");

        views.add(new Practice03TranslateView(context));
        titles.add("translate()");

        views.add(new Practice04ScaleView(context));
        titles.add("scale()");

        views.add(new Practice05RotateView(context));
        titles.add("rotate()");

        views.add(new Practice06SkewView(context));
        titles.add("skew()");

        views.add(new Practice07MatrixTranslateView(context));
        titles.add("MatrixTranslate");

        views.add(new Practice08MatrixScaleView(context));
        titles.add("MatrixScale");

        views.add(new Practice09MatrixRotateView(context));
        titles.add("MatrixRotate");

        views.add(new Practice10MatrixSkewView(context));
        titles.add("MatrixSkew");

        views.add(new Practice11CameraRotateView(context));
        titles.add("CameraRotate");

        views.add(new Practice12CameraRotateFixedView(context));
        titles.add("CameraRotateFixed");

        views.add(new Practice13CameraRotateHittingFaceView(context));
        titles.add("CameraRotateHittingFace");

        views.add(new Practice14FlipboardView(context));
        titles.add("Flipboard");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
