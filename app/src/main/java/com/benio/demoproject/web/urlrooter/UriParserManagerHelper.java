package com.benio.demoproject.web.urlrooter;

import android.net.Uri;

import com.benio.demoproject.web.urlrooter.parser.SalesTargetUriParser;

/**
 * Created by zhangzhibin on 2016/12/1.
 */
class UriParserManagerHelper {
    private static UriParserManager sInstance;

    public static UriParserManager getParserManager() {
        return sInstance;
    }

    /**
     * 初始化UriParser
     * 目前只是用path作为key
     * 后面可使用host+path的形式
     */
    static {
        sInstance = new UriParserManager(1);
        UriParserManager manager = sInstance;
        manager.register(H5Contract.SalesTarget.PATH, new SalesTargetUriParser());
    }

    public static UriParser getUriParser(Uri uri) {
        return sInstance.get(uri.getPath());
    }
}
