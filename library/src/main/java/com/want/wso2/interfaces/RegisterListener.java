package com.want.wso2.interfaces;

import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.TokenResponse;

/**
 * Created by wisn on 2017/8/21.
 */

public interface RegisterListener {
    /**
     * 注册成功
     * @param response
     * @param tokenResponse
     * @param code
     */
    void onSuccess(RegisterResponse response, TokenResponse tokenResponse, int code);

    /**
     *  注册失败
     * @param resonseStr
     * @param code
     */
    void onFailure(String resonseStr,int code);

    /**
     * 网络错误（没有网络）
     * @param msg
     */
    void netWorkError(String msg) ;

}
