package com.want.wso2.bean;

/**
 * Created by wisn on 2017/12/1.
 */

public class Register {
   public String registerUrl;
   public String tokenUrl;
   public String registrationProfileRequestjson;
   public String userName;
   public String password;
   public String scope;

    public Register(String registerUrl,
                    String tokenUrl,
                    String registrationProfileRequestjson,
                    String userName, String password, String scope) {
        this.registerUrl = registerUrl;
        this.tokenUrl = tokenUrl;
        this.registrationProfileRequestjson = registrationProfileRequestjson;
        this.userName = userName;
        this.password = password;
        this.scope = scope;
    }
}
