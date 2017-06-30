package com.benio.demoproject.common;

/**
 * 带参数的Filter
 * Created by zhangzhibin on 2017/6/8.
 */
public abstract class ParameterFilter<T, P> implements Filter<T> {

    private P mParam;

    public ParameterFilter() {
    }

    public ParameterFilter(P param) {
        mParam = param;
    }

    public void setParam(P param) {
        mParam = param;
    }

    @Override
    public boolean accept(T obj) {
        return accept(obj, mParam);
    }

    public abstract boolean accept(T obj, P param);
}
