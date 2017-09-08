package com.want.wso2.bean;

/**
 * Created by wisn on 2017/9/8.
 */

public class Fault {
    public int code;
    public String message;
    public String description;

    @Override
    public String toString() {
        return "Fault{" +
               "code=" + code +
               ", message='" + message + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
