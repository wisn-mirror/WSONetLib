package com.want.wso2.agent.library.bean;

/**
 * Created by wisn on 2017/8/21.
 */

public  class BaseNetBean<T> {
    public int code;
    public String msg;
    public Class<T> data;

    public BaseNetBean() {
    }

    public BaseNetBean(int code, String msg, Class<T> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Class<T> getData() {
        return data;
    }

    public void setData(Class<T> data) {
        this.data = data;
    }
}
