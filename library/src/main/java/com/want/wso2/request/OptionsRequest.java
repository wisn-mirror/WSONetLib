package com.want.wso2.request;

import com.want.wso2.base.BodyRequest;
import com.want.wso2.model.HttpMethod;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wisn on 2017/8/22.
 */

public class OptionsRequest<T> extends BodyRequest<T, OptionsRequest<T>> {

    public OptionsRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.OPTIONS;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}