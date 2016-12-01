package com.benio.demoproject.web.urlrooter;

/**
 * Url拦截器
 * Created by zhangzhibin on 2016/12/1.
 */
public interface UrlInterceptor {

    /**
     * 对url进行拦截
     *
     * @param url
     * @return true 则进行拦截
     */
    boolean intercept(String url);
}
