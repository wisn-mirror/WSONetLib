package com.want.wso2.adapter;

/**
 * Created by wisn on 2017/8/22.
 */

public interface CallAdapter<T, R> {

    /** call执行的代理方法 */
    R adapt(Call<T> call, AdapterParam param);
}