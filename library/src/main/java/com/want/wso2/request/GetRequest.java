package com.want.wso2.request;

import com.want.wso2.base.NoBodyRequest;
import com.want.wso2.model.HttpMethod;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wisn on 2017/8/22.
 */

public class GetRequest<T> extends NoBodyRequest<T, GetRequest<T>> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.get().url(url).tag(tag).build();
    }
}
