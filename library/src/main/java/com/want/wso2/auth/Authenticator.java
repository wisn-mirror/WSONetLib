package com.want.wso2.auth;

import com.want.wso2.WSONet;
import com.want.wso2.adapter.Call;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.Token;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.Callback;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.callback.TokenCallback;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.model.Response;
import com.want.wso2.utils.Base64Utils;
import com.want.wso2.utils.Constant;
import com.want.wso2.utils.TokenUtils;
import com.want.wso2.utils.WSOLog;
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
                                                       scope,body,registerListener);
//                                registerListener.onSuccess(response.body(), response.code());
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
                    @Override
                    public void netWorkError(String msg) {
                        super.netWorkError(msg);
                        registerListener.netWorkError(msg);
                    }
                }, false);
    }

    /**
     *
     * @param changePasswordUrl
     * @param passwordJson   : { "oldPassword":"aa","newPassword":"ddd"}
     * @param changePasswordCallBack
     */
    public static void changePassword(String changePasswordUrl, String  passwordJson,
                                      final ChangePasswordCallBack changePasswordCallBack){
        Token token = IdentityProxy.getInstance().isLogin();
        if(token==null){
            if(changePasswordCallBack!=null){
                changePasswordCallBack.onError(401,"Required OAuth credentials not provided. Make sure login in");
            }
            return ;
        }
        WSONet.<String>put(changePasswordUrl)
                .upJson(passwordJson)
                .execute(new JsonCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.isSuccessful()&&response.code()==200&&response.body()!=null){
                            if(changePasswordCallBack!=null){
                                changePasswordCallBack.onSuccess(response.code(),response.body());
                            }
                        }else{
                            try{
                                if(response.body()!=null){
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    String message = (String) jsonObject.get("message");
                                    if(changePasswordCallBack!=null){
                                        changePasswordCallBack.onError(response.code(),message);
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        try{
                            String message =null;
                            if(response.body()!=null){
                                JSONObject jsonObject = new JSONObject(response.body());
                                message = (String) jsonObject.get("message");
                            }
                            if(changePasswordCallBack!=null){
                                changePasswordCallBack.onError(response.code(),message);
                                return ;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void netWorkError(String msg) {
                        super.netWorkError(msg);
                        if(changePasswordCallBack!=null){
                            changePasswordCallBack.netWorkError(msg);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (IdentityProxy.getInstance().isLogin() != null) return true;
        return false;
    }

    /**
     *
     * @param unRegisterUrl
     * @param imei  手机的唯一串号
     */
    public static void loginOut(String unRegisterUrl,String imei) {
        Token token = IdentityProxy.getInstance().getToken();
        if(token==null)return ;
        WSONet.<String>delete(unRegisterUrl + "?applicationName=" + Constant.API_APPLICATION_NAME_PREFIX+ imei)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Accept", "*/*")
                .headers("Authorization","Bearer "+token.getAccessToken() )
                .headers("Content-Type", "application/json")
                .execute(new TokenCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        WSOLog.d("loginOut","onSuccess:"+response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        WSOLog.d("loginOut","onError:"+response.body());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        clearPassword();
                    }

                    @Override
                    public void netWorkError(String msg) {
                        super.netWorkError(msg);
                        clearPassword();
                    }
                    public void clearPassword(){
                        TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
                        tokenStore.clearAll();
                        IdentityProxy.getInstance().clearToken();
                    }

                }, false);
    }

    public void getToken(String url,
                         String clientId,
                         String clientSecret,
                         String userName,
                         String passWord,
                         String scope, final RegisterResponse body, final RegisterListener registerListener) {
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
                        tokenResponse(response, false);
                        if(response.isSuccessful()){
                            if(registerListener!=null){
                                TokenResponse body1 = response.body();
                                if(body1!=null){
                                    registerListener.onSuccess(body,body1,response.code());
                                }else{
                                    registerListener.onFailure("get token is failure",response.code());
                                }
                            }
                        }else{
                            if(registerListener!=null){
                                registerListener.onFailure("get token is failure",response.code());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<TokenResponse> response) {
                        super.onError(response);
                        IdentityProxy.getInstance().receiveAccessToken(500, "error", null);
                    }
                }, false);
    }

    public void refreshToken(final Call call,
                             final Callback callback,
                             String url,
                             String clientID,
                             String clientSecret,
                             Token token) {
        try {
            String basicAuthValue = "Basic " + base64(clientID, clientSecret);
            String
                    tokenUrl =
                    url +
                    "?refresh_token=" +
                    token.getRefreshToken() +
                    "&grant_type=refresh_token&scope=PRODUCTION";
            WSONet.<TokenResponse>post(tokenUrl)
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .headers("Authorization", basicAuthValue)
                    .execute(new TokenCallback<TokenResponse>() {
                        @Override
                        public void onSuccess(Response<TokenResponse> response) {
                            tokenResponse(response, true);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            if (call != null && callback != null) {
                                call.execute(callback);
                            }
                        }

                        @Override
                        public void onError(Response<TokenResponse> response) {
                            super.onError(response);
                            IdentityProxy.getInstance().receiveAccessToken(500, "error", null);
                        }
                    }, false);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * @param response
     */
    public void tokenResponse(Response<TokenResponse> response, boolean isRefresh) {
        TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
        if (response.isSuccessful() && response.code() == 200) {
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
            return Base64Utils.encode((obj1 + ":" + obj2).getBytes());
        }
        return null;
    }
}
