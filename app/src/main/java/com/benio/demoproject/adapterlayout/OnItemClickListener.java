package com.benio.demoproject.adapterlayout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangzhibin on 2017/4/13.
 */
public interface OnItemClickListener {

    void onItemClick(ViewGroup parent, View view, int position, long id);
}
