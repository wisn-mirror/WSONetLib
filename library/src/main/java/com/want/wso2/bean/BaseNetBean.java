package com.want.wso2.bean;

import java.io.Serializable;

/**
 * Created by wisn on 2017/8/21.
 */

public  class BaseNetBean<T> implements Serializable {
    public int code;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "BaseNetBean{" +
               "code=" + code +
               ", msg='" + msg + '\'' +
               ", data=" + data +
               '}';
    }
}
