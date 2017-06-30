package com.benio.demoproject.common;

/**
 * 过滤器
 * Created by zhangzhibin on 2017/6/7.
 */
public interface Filter<T> {

    boolean accept(T obj);
}
