package com.want.wso2.utils;

/**
 * Created by wisn on 2017/8/21.
 */

public class EmptyUtils {
    public static boolean isEmpty(String obj) {
        if (obj == null) return true;
        if ("" == obj.trim() || obj.trim().length() == 0) return true;
        return false;
    }
}
