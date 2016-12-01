package com.benio.demoproject.web.urlrooter;

import android.net.Uri;
import android.os.Bundle;

/**
 * uri解析器
 * Created by zhangzhibin on 2016/12/1.
 */
public interface UriParser {
    /**
     * 将uri中的参数转成Bundle对象
     *
     * @param uri
     * @return
     */
    Bundle parse(String uri);

    /**
     * 将uri中的参数转成Bundle对象
     *
     * @param uri
     * @return
     */
    Bundle parse(Uri uri);
}
