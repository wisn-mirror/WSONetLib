package com.want.wso2.interfaces;

/**
 * Created by wisn on 2017/10/12.
 *
 */

public interface LoginExpireCallBack {
    /**
     * 全局身份过期回调
     * @param tag 每个请求的tag ,可以通过tag可以区分哪个请求
     * @param code 每个请求回调的code  当请求前检查token==null时code=-1,请求后拦截请求会返回对应的code,403/401
     */
    void LoginExpire(String tag,int code);
}
