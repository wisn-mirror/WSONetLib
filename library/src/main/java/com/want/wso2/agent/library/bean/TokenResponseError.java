package com.want.wso2.agent.library.bean;

/**
 * Created by wisn on 2017/8/9.
 */

public class TokenResponseError {
    public String error_description;
    public String error;

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "TokenResponseError{" +
               "error_description='" + error_description + '\'' +
               ", error='" + error + '\'' +
               '}';
    }
}
