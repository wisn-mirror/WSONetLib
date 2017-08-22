package com.want.wso2.agent.library.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by wisn on 2017/8/21.
 */

public interface NetListener<T> {
    public abstract void onSuccess(T response,int code,String msg,String resonseStr);
    public abstract void onFailure(Throwable t,String resonseStr);

}
