package com.benio.demoproject.hencoder.practice8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benio.demoproject.R;

/**
 * http://hencoder.com/ui-2-1/
 * HenCoder Android UI 部分 2-1 布局基础
 * Created by zhangzhibin on 2017/11/16.
 */
public class PracticeDraw8Fragment extends Fragment {

    public static PracticeDraw8Fragment newInstance() {
        PracticeDraw8Fragment fragment = new PracticeDraw8Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_practice_draw8, container, false);
    }
}
