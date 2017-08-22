package com.want.wso2.agent.library.bean;

/**
 * Created by wisn on 2017/8/9.
 */

public class DeviceResponse extends Bean {
    public String responseCode;
    public String responseMessage;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "DeviceResponse{" +
               "responseCode='" + responseCode + '\'' +
               ", responseMessage='" + responseMessage + '\'' +
               '}';
    }
}
