package com.benio.demoproject.web.urlrooter;

import android.net.Uri;
import android.support.v4.util.SimpleArrayMap;

/**
 * 管理维护{@link UriParser}
 * Created by zhangzhibin on 2016/12/1.
 */
public class UriParserManager {

    private SimpleArrayMap<String, UriParser> mParsers;

    public static UriParserManager getInstance() {
        return UriParserManagerHelper.getParserManager();
    }

    public UriParserManager() {
        this(0);
    }

    public UriParserManager(int capacity) {
        mParsers = new SimpleArrayMap<>(capacity);
    }

    public UriParser register(String key, UriParser parser) {
        if (parser != null) {
            mParsers.put(key, parser);
        }
        return parser;
    }

    public UriParser unregister(String key) {
        return mParsers.remove(key);
    }

    public UriParser get(String key) {
        return mParsers.get(key);
    }

    public UriParser get(Uri uri) {
        return UriParserManagerHelper.getUriParser(uri);
    }
}
