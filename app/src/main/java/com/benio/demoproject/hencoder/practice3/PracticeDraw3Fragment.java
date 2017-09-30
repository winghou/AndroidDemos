package com.benio.demoproject.hencoder.practice3;

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
 * http://hencoder.com/ui-1-3/
 * HenCoder Android 开发进阶：自定义 View 1-3 drawText() 文字的绘制
 * Created by zhangzhibin on 2017/9/29.
 */
public class PracticeDraw3Fragment extends Fragment {

    public static PracticeDraw3Fragment newInstance() {
        PracticeDraw3Fragment fragment = new PracticeDraw3Fragment();
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

        views.add(new Practice01DrawTextView(context));
        titles.add("drawText()");

        views.add(new Practice02StaticLayoutView(context));
        titles.add("StaticLayout");

        views.add(new Practice03SetTextSizeView(context));
        titles.add("setTextSize()");

        views.add(new Practice04SetTypefaceView(context));
        titles.add("setTypeface()");

        views.add(new Practice05SetFakeBoldTextView(context));
        titles.add("setFakeBoldText()");

        views.add(new Practice06SetStrikeThruTextView(context));
        titles.add("setStrikeThruText()");

        views.add(new Practice07SetUnderlineTextView(context));
        titles.add("setUnderlineText()");

        views.add(new Practice08SetTextSkewXView(context));
        titles.add("setTextSkewX()");

        views.add(new Practice09SetTextScaleXView(context));
        titles.add("setTextScaleX()");

        views.add(new Practice10SetTextAlignView(context));
        titles.add("setTextAlign()");

        views.add(new Practice11GetFontSpacingView(context));
        titles.add("getFontSpacing()");

        views.add(new Practice12MeasureTextView(context));
        titles.add("measureText()");

        views.add(new Practice13GetTextBoundsView(context));
        titles.add("getTextBounds()");

        views.add(new Practice14GetFontMetricsView(context));
        titles.add("getFontMetrics()");

        views.add(new Practice15SetLetterSpacingView(context));
        titles.add("setLetterSpacing()");

        views.add(new Practice16SetFontFeatureSettingsView(context));
        titles.add("setFontFeatureSettings()");

        views.add(new Practice17SetTextLocaleView(context));
        titles.add("setTextLocale()");

        views.add(new Practice18MeasureTextView(context));
        titles.add("measureText()");

        views.add(new Practice19BreakTextView(context));
        titles.add("breakText()");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
