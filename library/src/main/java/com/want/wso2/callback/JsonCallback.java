package com.want.wso2.callback;

import com.want.wso2.auth.APIAccessCallBack;
import com.want.wso2.auth.IdentityProxy;
import com.want.wso2.base.Request;
import com.want.wso2.bean.Token;
import com.want.wso2.convert.JsonConvert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;

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
        IdentityProxy.getInstance().checkToken(new APIAccessCallBack() {
            @Override
            public void onAPIAccessReceive(String status, Token token) {
                request.headers("Authorization", "Bearer "+token.getAccessToken());
                request.headers("User-Agent", "Mozilla/5.0 ( compatible ), Android");
                request.headers("Accept", "application/json");
                request.headers("Content-Type", "application/json");
            }
        });

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
}