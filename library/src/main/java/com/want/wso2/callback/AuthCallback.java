package com.want.wso2.callback;

import com.want.wso2.base.Request;
import com.want.wso2.convert.JsonConvert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by wisn on 2017/8/22.
 */

public abstract class AuthCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public AuthCallback() {
    }

    public AuthCallback(Type type) {
        this.type = type;
    }

    public AuthCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(final Request<T, ? extends Request> request) {
        super.onStart(request);
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }
    /**
     * 无网络错误
     * @param msg
     */
    public void netWorkError(String msg){

    }
}