package com.want.wso2.exception;

/**
 * Created by wisn on 2017/8/22.
 */

public class WSONetException extends Exception {
    private static final long serialVersionUID = -8641198158155821498L;

    public WSONetException(String detailMessage) {
        super(detailMessage);
    }

    public static WSONetException UNKNOWN() {
        return new WSONetException("unknown exception!");
    }

    public static WSONetException BREAKPOINT_NOT_EXIST() {
        return new WSONetException("breakpoint file does not exist!");
    }

    public static WSONetException BREAKPOINT_EXPIRED() {
        return new WSONetException("breakpoint file has expired!");
    }
}
