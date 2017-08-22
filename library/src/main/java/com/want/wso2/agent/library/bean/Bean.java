package com.want.wso2.agent.library.bean;

import com.want.wso2.agent.library.utils.JsonTool;

/**
 * Created by wisn on 2017/8/9.
 */

public class Bean {
    public String toJSON(){
        return JsonTool.toJson(this);
    }
}
