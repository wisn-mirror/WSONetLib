package com.want.wso2.model;

/**
 * Created by wisn on 2017/8/22.
 */

public interface Priority {
    int UI_TOP = Integer.MAX_VALUE;
    int UI_NORMAL = 1000;
    int UI_LOW = 100;
    int DEFAULT = 0;
    int BG_TOP = -100;
    int BG_NORMAL = -1000;
    int BG_LOW = Integer.MIN_VALUE;
}
