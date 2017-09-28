package com.want.wso2.callback;

import com.want.wso2.base.Request;
import com.want.wso2.model.Progress;
import com.want.wso2.model.Response;
import com.want.wso2.utils.WSOLog;

/**
 * Created by wisn on 2017/8/22.
 */

public abstract class AbsCallback<T> implements Callback<T> {

    @Override
    public void onStart(Request<T, ? extends Request> request) {
    }

    @Override
    public void onCacheSuccess(Response<T> response) {
    }

    @Override
    public void onError(Response<T> response) {
        WSOLog.printStackTrace(response.getException());
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void uploadProgress(Progress progress) {
    }

    @Override
    public void downloadProgress(Progress progress) {
    }
    public void netWorkError(String msg){

    }
}