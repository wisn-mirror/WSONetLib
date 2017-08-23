package com.want.wso2.convert;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wisn on 2017/8/22.
 */

public class StringConvert implements Converter<String> {

    @Override
    public String convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) return null;
        return body.string();
    }
}
