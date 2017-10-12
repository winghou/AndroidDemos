package com.benio.demoproject.hencoder.practice7;

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
 * http://hencoder.com/ui-1-7/
 * HenCoder Android 自定义 View 1-7：属性动画 Property Animation（进阶篇）
 * Created by zhangzhibin on 2017/10/11.
 */
public class PracticeDraw7Fragment extends Fragment {

    public static PracticeDraw7Fragment newInstance() {
        PracticeDraw7Fragment fragment = new PracticeDraw7Fragment();
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

        views.add(new Practice01ArgbEvaluatorLayout(context));
        titles.add("ArgbEvaluator");

        views.add(new Practice02HsvEvaluatorLayout(context));
        titles.add("HsvEvaluator");

        views.add(new Practice03OfObjectLayout(context));
        titles.add("OfObject1");

        views.add(new Practice04PropertyValuesHolderLayout(context));
        titles.add("PropertyValuesHolder");

        views.add(new Practice05AnimatorSetLayout(context));
        titles.add("AnimatorSet");

        views.add(new Practice06KeyframeLayout(context));
        titles.add("Keyframe");

        PagerAdapter adapter = new ViewPagerAdapter(views, titles);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }
}
