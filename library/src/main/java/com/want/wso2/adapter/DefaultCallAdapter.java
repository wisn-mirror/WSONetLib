package com.want.wso2.adapter;

/**
 * Created by wisn on 2017/8/22.
 */

public class DefaultCallAdapter<T> implements CallAdapter<T, Call<T>> {

    @Override
    public Call<T> adapt(Call<T> call, AdapterParam param) {
        return call;
    }
}