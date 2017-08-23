package com.want.wso2.upload;

import com.want.wso2.interfaces.ProgressListener;

/**
 * Created by wisn on 2017/8/23.
 */

public abstract class UploadListener<T> implements ProgressListener<T> {

    public final Object tag;

    public UploadListener(Object tag) {
        this.tag = tag;
    }
}
