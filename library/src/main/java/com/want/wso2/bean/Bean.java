package com.want.wso2.bean;

import com.want.wso2.utils.JsonTool;

/**
 * Created by wisn on 2017/8/9.
 */

public class Bean {
    public String toJSON(){
        return JsonTool.toJson(this);
    }
}
