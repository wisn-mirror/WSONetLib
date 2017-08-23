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
package com.want.wso2;

import com.want.wso2.bean.Token;

public class IdentityProxyBackup {/*


    public static String clientID;
    public static String clientSecret;
    private static String TAG = "IdentityProxyBackup";
    private static Token token = null;
    private static IdentityProxyBackup identityProxy = new IdentityProxyBackup();
    private Context context;
    private static String accessTokenURL;
    private APIAccessCallBack apiAccessCallBack;
    private TokenCallBack tokenCallBack;
    private int requestCode = 0;

    private IdentityProxyBackup() {}

    public static synchronized IdentityProxyBackup getInstance() {
        return identityProxy;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public synchronized void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getAccessTokenURL() {
        return accessTokenURL;
    }

    public void setAccessTokenURL(String accessTokenURL) {
        IdentityProxyBackup.accessTokenURL = accessTokenURL;
    }

    @Override
    public void receiveAccessToken(String status, String message, Token token) {
        if (Constants.DEBUG_ENABLED && token != null) {
            Log.d(TAG, "receiveAccessToken");
        }
        IdentityProxyBackup.token = token;
        apiAccessCallBack.onAPIAccessReceive(status,token);
    }

    @Override
    public void receiveNewAccessToken(String status, String message, Token token) {
        if (Constants.DEBUG_ENABLED && token != null) {
            Log.d(TAG, "receiveNewAccessToken");
        }
        IdentityProxyBackup.token = token;
        tokenCallBack.onReceiveTokenResult(token, status);
    }


    *//**
     * Initializing the IDP plugin and obrtaining the access token.
     *
     * @param info              - Includes token end point and Oauth app credentials.
     * @param apiAccessCallBack - Callback when API access happens.
     * @param context        - Application context.
     *//*

    public void init(CredentialInfo info, APIAccessCallBack apiAccessCallBack, Context context) {
        if (Constants.DEBUG_ENABLED) {
            Log.d(TAG, "init");
        }
        IdentityProxyBackup.clientID = info.getClientID();
        IdentityProxyBackup.clientSecret = info.getClientSecret();
        this.apiAccessCallBack = apiAccessCallBack;
        this.context = context;
        SharedPreferences mainPref = context.getSharedPreferences(Constants.APPLICATION_PACKAGE
                , Context.MODE_PRIVATE);
        Editor editor = mainPref.edit();
        editor.putString(Constants.CLIENT_ID, clientID);
        editor.putString(Constants.CLIENT_SECRET, clientSecret);
        editor.putString(Constants.TOKEN_ENDPOINT, info.getTokenEndPoint());
        editor.commit();
        setAccessTokenURL(info.getTokenEndPoint());
        AccessTokenHandler accessTokenHandler = new AccessTokenHandler(info, this);
        accessTokenHandler.obtainAccessToken();
    }

    public void requestToken(Context context, TokenCallBack tokenCallBack, String clientID,
                             String clientSecret) {
        this.context = context;
        this.tokenCallBack = tokenCallBack;
        IdentityProxyBackup.clientID = clientID;
        IdentityProxyBackup.clientSecret = clientSecret;
        if (Constants.DEBUG_ENABLED) {
            Log.d(TAG, "requestToken called.");
            if(IdentityProxyBackup.clientID == null || IdentityProxyBackup.clientSecret == null) {
                Log.d(TAG, "Client credentials are null.");
            }
        }
        if (token == null) {
            if (Constants.DEBUG_ENABLED) {
                Log.d(TAG, "token is null.");
            }
            validateStoredToken();
        } else {
            boolean isExpired = ServerUtilities.isValid(token.getDate());
            if (Constants.DEBUG_ENABLED) {
                Log.d(TAG, "token is expired "+ isExpired);
                Log.d(TAG, "token expiry "+ token.getDate().toString());
            }
            if (!isExpired) {
                synchronized(this){
                    IdentityProxyBackup.getInstance().receiveNewAccessToken(Constants.REQUEST_SUCCESSFUL,
                                                                            "success", token);
                }
            } else {
                validateStoredToken();
            }
        }
    }

    private void validateStoredToken() {
        if (Constants.DEBUG_ENABLED) {
            Log.d(TAG, "validateStoredToken.");
        }
        SharedPreferences mainPref = context.getSharedPreferences(Constants.APPLICATION_PACKAGE,
                Context.MODE_PRIVATE);
        String refreshToken = mainPref.getString(Constants.REFRESH_TOKEN, null).toString();
        String accessToken = mainPref.getString(Constants.ACCESS_TOKEN, null).toString();
        String date = mainPref.getString(Constants.DATE_LABEL, null).toString();
        String endPoint = mainPref.getString(Constants.TOKEN_ENDPOINT, null).toString();
        setAccessTokenURL(endPoint);

        if (!refreshToken.isEmpty()) {
            if (Constants.DEBUG_ENABLED) {
                Log.d(TAG, "refreshToken is not empty.");
            }
            token = new Token();
            token.setDate(date);
            token.setRefreshToken(refreshToken);
            token.setAccessToken(accessToken);
            boolean isExpired = ServerUtilities.isValid(token.getDate());
            if (!isExpired) {
                if (Constants.DEBUG_ENABLED) {
                    Log.d(TAG, "stored token is not expired.");
                }
                synchronized(this){
                    IdentityProxyBackup.getInstance().receiveNewAccessToken(Constants.REQUEST_SUCCESSFUL,
                                                                            Constants.SUCCESS_RESPONSE,
                                                                            token);
                }
            } else {
                if (Constants.DEBUG_ENABLED) {
                    Log.d(TAG, "stored token is expired, refreshing");
                }
                refreshToken();
            }
        } else {
            if (Constants.DEBUG_ENABLED) {
                Log.d(TAG, "refreshToken is empty.");
            }
            synchronized(this){
                IdentityProxyBackup.getInstance().receiveNewAccessToken(Constants.ACCESS_FAILURE,
                                                                        Constants.FAILURE_RESPONSE, token);
            }
        }
    }

    public void refreshToken() {
        RefreshTokenHandler refreshTokenHandler = new RefreshTokenHandler(token);
        refreshTokenHandler.obtainNewAccessToken();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
*/
}
