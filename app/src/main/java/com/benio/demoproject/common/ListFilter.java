package com.benio.demoproject.common;

import java.util.List;

/**
 * Created by zhangzhibin on 2017/5/12.
 */
public interface ListFilter<T> {
    List<T> filter(List<T> list);
}
