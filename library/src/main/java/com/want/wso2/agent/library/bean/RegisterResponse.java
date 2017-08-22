package com.want.wso2.agent.library.bean;

/**
 * Created by wisn on 2017/8/9.
 */

public class RegisterResponse extends Bean{
    public String client_secret;
    public String client_id;

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
               "client_secret='" + client_secret + '\'' +
               ", client_id='" + client_id + '\'' +
               '}';
    }
}
