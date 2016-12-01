package com.benio.demoproject.web.urlrooter.parser;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.benio.demoproject.web.urlrooter.H5Contract;
import com.benio.demoproject.web.urlrooter.UriParser;

import java.util.Set;

/**
 * Created by zhangzhibin on 2016/12/1.
 */
public class SalesTargetUriParser implements UriParser {
    private static final String TAG = "UriParser";

    @Override
    public Bundle parse(String uri) {
        return TextUtils.isEmpty(uri) ? null : parse(Uri.parse(uri));
    }

    @Override
    public Bundle parse(Uri uri) {
        if (uri == null) {
            return null;
        }
        Log.d(TAG, "host: " + uri.getHost() + " , path: " + uri.getPath());
        if (H5Contract.SalesTarget.HOST.equals(uri.getHost())) {
            if (H5Contract.SalesTarget.PATH.equals(uri.getPath())) {
                Bundle bundle = new Bundle();
                Set<String> keySet = uri.getQueryParameterNames();
                for (String key : keySet) {
                    String value = uri.getQueryParameter(key);
                    bundle.putString(key, value);
                }
                return bundle;
            }
        }
        return null;
    }
}
