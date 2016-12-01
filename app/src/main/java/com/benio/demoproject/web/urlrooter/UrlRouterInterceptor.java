package com.benio.demoproject.web.urlrooter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by zhangzhibin on 2016/12/1.
 */
public class UrlRouterInterceptor implements UrlInterceptor {
    private Context mContext;

    public UrlRouterInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public boolean intercept(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (intercept(uri)) {
            Bundle params = null;
            UriParser parser = UriParserManager.getInstance().get(uri);
            if (parser != null) {
                //解析出uri中包含的参数
                params = parser.parse(uri);
            }
            // 路由跳转
            UrlRouter.from(mContext).params(params).jump(uri);
            return true;
        }
        return false;
    }

    protected boolean intercept(Uri uri) {
        // 只拦截scheme = magapp 的url
        return uri.getScheme().equals(H5Contract.SCHEMA);
    }
}
