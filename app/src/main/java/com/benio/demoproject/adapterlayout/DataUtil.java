package com.benio.demoproject.adapterlayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhibin on 2017/12/5.
 */
public class DataUtil {

    public static List<String> getStringList(int size) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }
}
