package com.want.wso2.interfaces;

import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.TokenResponse;

/**
 * Created by wisn on 2017/8/21.
 */

public interface RegisterListener {
    void onSuccess(RegisterResponse response, TokenResponse tokenResponse, int code);
    void onFailure(String resonseStr,int code);

}
