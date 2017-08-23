package com.want.wso2.request;

import com.want.wso2.base.BodyRequest;
import com.want.wso2.model.HttpMethod;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wisn on 2017/8/22.
 */

public class PatchRequest<T> extends BodyRequest<T, PatchRequest<T>> {

    public PatchRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.PATCH;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.patch(requestBody).url(url).tag(tag).build();
    }
}
