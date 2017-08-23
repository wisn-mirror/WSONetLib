package com.want.wso2.download;

import com.want.wso2.interfaces.ProgressListener;

import java.io.File;

/**
 * Created by wisn on 2017/8/23.
 */

public abstract class DownloadListener implements ProgressListener<File> {

    public final Object tag;

    public DownloadListener(Object tag) {
        this.tag = tag;
    }
}
