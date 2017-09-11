package com.want.wso2.auth;

import com.google.gson.Gson;
import com.want.wso2.WSONet;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.Token;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.TokenCallback;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.model.Response;
import com.want.wso2.utils.TokenUtils;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by wisn on 2017/8/21.
 */

public class Authenticator {
    private static String[] SUBSCRIBED_API = new String[]{"android"};
    public final static String SCOPES = "default appm:read" +
                                        " perm:android:enroll perm:android:disenroll" +
                                        " perm:android:view-configuration perm:android:manage-configuration";

    /**
     * 登录
     *
     * @param registerUrl
     * @param tokenUrl
     * @param registrationProfileRequest
     * @param userName
     * @param password
     * @param scope
     * @param registerListener
     */
    public static void register(String registerUrl,
                                final String tokenUrl,
                                RegistrationProfileRequest registrationProfileRequest,
                                final String userName,
                                final String password,
                                final String scope,
                                final RegisterListener registerListener) {
        if (registrationProfileRequest != null) {
            if (registrationProfileRequest.getTags() == null) {
                registrationProfileRequest.setTags(SUBSCRIBED_API);
            }
        }
        String basicAuthValue = "Basic " + base64(userName, password);
        WSONet.<RegisterResponse>post(registerUrl)
                .upObjectToJson(registrationProfileRequest)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/json")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new TokenCallback<RegisterResponse>() {
                    @Override
                    public void onSuccess(Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            RegisterResponse body = response.body();
                            if (body != null) {
                                TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
                                tokenStore.saveIdSecrect(body.getClient_id(), body.getClient_secret());
                                tokenStore.saveTokenUrl(tokenUrl);
                                Authenticator authenticator = new Authenticator();
                                authenticator.getToken(tokenUrl,
                                                       body.getClient_id(),
                                                       body.getClient_secret(),
                                                       userName,
                                                       password,
                                                       scope);
                                registerListener.onSuccess(response.body(), response.code());
                            } else {
                                registerListener.onFailure(" RegisterResponse is null ", response.code());
                            }
                        } else {
                            registerListener.onFailure("response is error ", response.code());
                        }
                    }

                    @Override
                    public void onError(Response<RegisterResponse> response) {
                        super.onError(response);
                        registerListener.onFailure("register error", response.code());
                    }
                });
    }

    /**
     * loginOut
     */
    public static void loginOut(){
        // TODO: 2017/9/11 loginOut
       /* TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
        IdentityProxy.getInstance()*/

    }

    public void getToken(String url,
                         String clientId,
                         String clientSecret,
                         String userName,
                         String passWord,
                         String scope) {
        if (scope == null) {
            scope = SCOPES;
        }
        String basicAuthValue = "Basic " + base64(clientId, clientSecret);
        String tokenUrl = url + "?username=" + userName + "&password="
                          + passWord + "&grant_type="
                          + "password" + "&scope=" + scope;
        WSONet.<TokenResponse>post(tokenUrl)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new TokenCallback<TokenResponse>() {
                    @Override
                    public void onSuccess(Response<TokenResponse> response) {
                        tokenResponse(response, true);
                    }

                    @Override
                    public void onError(Response<TokenResponse> response) {
                        super.onError(response);
                        IdentityProxy.getInstance().receiveAccessToken(500, "error", null);
                    }
                });

    }

    public void refreshToken(String url, String clientID, String clientSecret, Token token) {

        String basicAuthValue = "Basic " + base64(clientID, clientSecret);
        String
                tokenUrl =
                url +
                "?refresh_token=" +
                token.getRefreshToken() +
                "&grant_type=refresh_token&scope=PRODUCTION";
        WSONet.<TokenResponse>post(tokenUrl)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new TokenCallback<TokenResponse>() {
                    @Override
                    public void onSuccess(Response<TokenResponse> response) {
                        tokenResponse(response, false);
                    }

                    @Override
                    public void onError(Response<TokenResponse> response) {
                        super.onError(response);
                        IdentityProxy.getInstance().receiveAccessToken(500, "error", null);
                    }
                });
    }


    /**
     * @param response
     */
    public void tokenResponse(Response<TokenResponse> response, boolean isRefresh) {
        TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
        if (response.isSuccessful()) {
            TokenResponse body = response.body();
            Token token = new Token();
            String
                    expireDate = TokenUtils.dateFormat.format(new Date().getTime() +
                                                              (Integer.parseInt(body.getExpires_in()) *
                                                               1000));
            token.setDate(expireDate);
            token.setRefreshToken(body.getRefresh_token());
            token.setAccessToken(body.getAccess_token());
            token.setExpired(false);
            tokenStore.saveToken(token);
            if (isRefresh) {
                IdentityProxy.getInstance().receiveAccessToken(200, "success", token);
            } else {
                IdentityProxy.getInstance().receiveNewAccessToken(200, "success", token);
            }
        } else {
            IdentityProxy.getInstance().receiveAccessToken(500, "error", null);
        }
    }

    public static String base64(String obj1, String obj2) {
        if (obj1 != null && obj2 != null) {
            return new String(Base64.encodeBase64((obj1 + ":" + obj2).getBytes()));
        }
        return null;
    }
}
