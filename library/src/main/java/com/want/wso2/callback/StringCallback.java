package com.want.wso2.callback;

import com.want.wso2.convert.StringConvert;

import okhttp3.Response;

/**
 * Created by wisn on 2017/8/22.
 */

public abstract class StringCallback extends AbsCallback<String> {

    private StringConvert convert;

    public StringCallback() {
        convert = new StringConvert();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }
}
