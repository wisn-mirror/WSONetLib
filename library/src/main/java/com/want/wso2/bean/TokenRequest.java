/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.want.wso2.bean;

/**
 * This class represents credential information required when invoking
 * OAuth token end-point.
 */
public class TokenRequest  extends Bean{
    private String username = null;
    private String password = null;
    private String clientID = null;
    private String clientSecret = null;
    private String tokenEndPoint = null;
    private String tenantDomain = null;
    private String scope=null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTokenEndPoint() {
        return tokenEndPoint;
    }

    public void setTokenEndPoint(String tokenEndPoint) {
        this.tokenEndPoint = tokenEndPoint;
    }

    public String getTenantDomain() {
        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "TokenRequest{" +
               "username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", clientID='" + clientID + '\'' +
               ", clientSecret='" + clientSecret + '\'' +
               ", tokenEndPoint='" + tokenEndPoint + '\'' +
               ", tenantDomain='" + tenantDomain + '\'' +
               ", scope='" + scope + '\'' +
               '}';
    }
    public String getPrem(){
        return "username="+username+"&password="+password+"&grant_type=password&scope="+scope;
    }
}
