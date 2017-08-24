package com.want.wso2.interfaces;

import com.want.wso2.bean.RegisterResponse;

/**
 * Created by wisn on 2017/8/21.
 */

public interface RegisterListener {
    void onSuccess(RegisterResponse response,int code);
    void onFailure(String resonseStr,int code);

}
