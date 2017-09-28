package com.want.wso2.auth;

import com.want.wso2.bean.ChangePasswordResponse;
import com.want.wso2.model.Response;

/**
 * Created by wisn on 2017/8/24.
 */

public interface ChangePasswordCallBack {
     void onSuccess(int code,String response) ;
     void onError(int code,String msg) ;
     void netWorkError(String msg) ;
}
