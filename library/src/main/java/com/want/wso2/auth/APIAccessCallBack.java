package com.want.wso2.auth;

import com.want.wso2.bean.Token;

/**
 * Created by wisn on 2017/8/24.
 */

public interface APIAccessCallBack {
    void onAPIAccessReceive(String status,int code,Token token);
}

