package com.want.wso2.callback;

import com.want.wso2.WSONet;
import com.want.wso2.auth.IdentityProxy;
import com.want.wso2.base.Request;
import com.want.wso2.bean.Token;
import com.want.wso2.convert.JsonConvert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by wisn on 2017/8/22.
 */

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(final Request<T, ? extends Request> request) {
        super.onStart(request);
        request.headers("User-Agent", "Mozilla/5.0 ( compatible ), Android");
        request.headers("Accept", "application/json");
        request.headers("Content-Type", "application/json");
        Token token = IdentityProxy.getInstance().getToken();
        if (token != null) {
            request.headers("Authorization", "Bearer " + token.getAccessToken());
        }
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
        if (response.code() == 401 || response.code() == 403) {
            String tag = null;
            if (response.request() != null && response.request().tag() != null)
                tag = response.request().tag().toString();
            WSONet.getInstance().loginExpireCallBack(tag);
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    public void netWorkError(String msg) {

    }
}