package com.want.wso2.auth;

import com.want.wso2.WSONet;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenRequest;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.model.Response;

import org.apache.commons.codec.binary.Base64;


/**
 * Created by wisn on 2017/8/21.
 */

public class Authenticator {

    public static void register(String url, RegistrationProfileRequest registrationProfileRequest,
                                String userName,
                                String password) {
        String basicAuthValue = "Basic " + base64(userName, password);
        WSONet.<RegisterResponse>post(url)
                .upObjectToJson(registrationProfileRequest)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/json")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new JsonCallback<RegisterResponse>() {
                    @Override
                    public void onSuccess(Response<RegisterResponse> response) {

                    }

                    @Override
                    public void onError(Response<RegisterResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public static  void getToken(String url, TokenRequest tokenRequest) {
        String basicAuthValue = "Basic " + base64(tokenRequest.getClientID(), tokenRequest
                .getClientSecret());
        String tokenUrl = url + "?username=" + tokenRequest.getUsername() + "&password="
                          + tokenRequest.getPassword() + "&grant_type="
                          + "password" + "&scope="
                          + tokenRequest.getScope();
        WSONet.<TokenResponse>post(tokenUrl)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new JsonCallback<TokenResponse>() {
                    @Override
                    public void onSuccess(Response<TokenResponse> response) {

                    }

                    @Override
                    public void onError(Response<TokenResponse> response) {
                        super.onError(response);
                    }
                });

    }

    public void refreshToken(String url, String clientID, String clientSecret, String refresh_token) {

        String basicAuthValue = "Basic " + base64(clientID, clientSecret);
        String
                tokenUrl =
                url + "?refresh_token=" + refresh_token + "&grant_type=refresh_token&scope=PRODUCTION";
        WSONet.<TokenResponse>post(tokenUrl)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new JsonCallback<TokenResponse>() {
                    @Override
                    public void onSuccess(Response<TokenResponse> response) {

                    }

                    @Override
                    public void onError(Response<TokenResponse> response) {
                        super.onError(response);
                    }
                });
    }

    public static String base64(String obj1, String obj2) {
        if (obj1 != null && obj2 != null) {
            return new String(Base64.encodeBase64((obj1 + ":" + obj2).getBytes()));
        }
        return null;
    }
}
