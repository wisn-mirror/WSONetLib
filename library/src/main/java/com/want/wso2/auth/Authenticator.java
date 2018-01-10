package com.want.wso2.auth;

import com.want.wso2.WSONet;
import com.want.wso2.bean.Register;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.Token;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.callback.AuthCallback;
import com.want.wso2.callback.JsonCallback;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.model.Response;
import com.want.wso2.utils.Base64Utils;
import com.want.wso2.utils.Constant;
import com.want.wso2.utils.Convert;
import com.want.wso2.utils.TokenUtils;
import com.want.wso2.utils.WSOLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.ResponseBody;


/**
 * Created by wisn on 2017/8/21.
 */

public class Authenticator {
    private static String TAG = "Authenticator";
    private static String[] SUBSCRIBED_API = new String[]{"android"};
    public final static String SCOPES = "default appm:read" +
                                        " perm:android:enroll perm:android:disenroll" +
                                        " perm:android:view-configuration perm:android:manage-configuration";

    /**
     * 认证
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
                                String tokenUrl,
                                RegistrationProfileRequest registrationProfileRequest,
                                String userName,
                                String password,
                                String scope,
                                RegisterListener registerListener) {
        if (registrationProfileRequest != null && registrationProfileRequest.getTags() == null) {
            registrationProfileRequest.setTags(SUBSCRIBED_API);
        }
        String registrationProfileRequestjson = Convert.toJson(registrationProfileRequest);
        registerByRefreshtokenExpire(registerUrl,
                                     tokenUrl,
                                     registrationProfileRequestjson,
                                     userName,
                                     password,
                                     scope,
                                     registerListener);

    }

    /**
     * @param registerUrl
     * @param tokenUrl
     * @param registrationProfileRequest
     * @param userName
     * @param password
     * @param scope
     * @param registerListener
     */
    protected static void registerByRefreshtokenExpire(final String registerUrl,
                                                       final String tokenUrl,
                                                       final String registrationProfileRequest,
                                                       final String userName,
                                                       final String password,
                                                       final String scope,
                                                       final RegisterListener registerListener) {
        String basicAuthValue = "Basic " + base64(userName, password);
        WSONet.<RegisterResponse>post(registerUrl)
                .upJson(registrationProfileRequest)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Content-Type", "application/json")
                .headers("Accept", "application/json")
                .headers("Authorization", basicAuthValue)
                .execute(new AuthCallback<RegisterResponse>() {
                    @Override
                    public void onSuccess(Response<RegisterResponse> response) {
                        if (response.isSuccessful()&&(response.code()==201||response.code()==200)) {
                            RegisterResponse body = response.body();
                            if (body != null) {
                                RegisterStore
                                        registerStore =
                                        new RegisterStore(WSONet.getInstance().getContext());
                                registerStore.saveRegister(new Register(registerUrl,
                                                                        tokenUrl,
                                                                        registrationProfileRequest,
                                                                        userName,
                                                                        password,
                                                                        scope));
                                TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
                                tokenStore.saveIdSecrect(body.getClient_id(), body.getClient_secret());
                                tokenStore.saveTokenUrl(tokenUrl);
                                Authenticator authenticator = new Authenticator();
                                authenticator.getToken(tokenUrl,
                                                       body.getClient_id(),
                                                       body.getClient_secret(),
                                                       userName,
                                                       password,
                                                       scope, body, registerListener);
                            } else {
                                registerListener.onFailure(response.message(), response.code());
                            }
                        } else {

                            registerListener.onFailure(response.message(), response.code());
                        }
                    }

                    @Override
                    public void onError(Response<RegisterResponse> response) {
                        super.onError(response);
                        if(response!=null&&response.getRawResponse()!=null){
                            ResponseBody body = response.getRawResponse().body();
                            if(body!=null){
                                try {
                                    byte[] bytes = body.bytes();
                                    registerListener.onFailure(new String(bytes), response.code());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                registerListener.onFailure(response.getException().toString(), response.code());
                            }
                        }else{
                            registerListener.onFailure(response.getException().toString(), response.code());
                        }
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
     * @param oldPassword
     * @param newPassword
     * @param changePasswordCallBack
     */
    public static void changePassword(String changePasswordUrl, String oldPassword, final String newPassword,
                                      final ChangePasswordCallBack changePasswordCallBack) {
        Token token = IdentityProxy.getInstance().isLogin();
        if (token == null) {
            if (changePasswordCallBack != null) {
                changePasswordCallBack.onError(401,
                                               "Required OAuth credentials not provided. Make sure login in");
            }
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldPassword", oldPassword);
            jsonObject.put("newPassword", newPassword);
            WSONet.<String>put(changePasswordUrl)
                    .upJson(jsonObject)
                    .execute(new JsonCallback<String>() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response.isSuccessful()&&(response.code()==201||response.code()==200)) {
                                if (changePasswordCallBack != null) {
                                    changePasswordCallBack.onSuccess(response.code(), response.body());
                                }
                                //更新新的密码，用于注册
                                RegisterStore
                                        registerStore =
                                        new RegisterStore(WSONet.getInstance().getContext());
                                registerStore.changePassword(newPassword);
                            } else {
                                String message = null;
                                try {
                                    if (response.body() != null) {
                                        JSONObject jsonObject = new JSONObject(response.body());
                                        message = (String) jsonObject.get("message");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (changePasswordCallBack != null) {
                                        changePasswordCallBack.onError(response.code(), message);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            String message = null;
                            try {
                                if (response.body() != null) {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    message = (String) jsonObject.get("message");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (changePasswordCallBack != null) {
                                    changePasswordCallBack.onError(response.code(), message);
                                }
                            }
                        }

                        @Override
                        public void netWorkError(String msg) {
                            super.netWorkError(msg);
                            if (changePasswordCallBack != null) {
                                changePasswordCallBack.netWorkError(msg);
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            if (changePasswordCallBack != null) {
                changePasswordCallBack.onError(-1,
                                               "Please make sure input password");
            }
        }
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
     * @param unRegisterUrl
     * @param applicationName          applicationName+手机的唯一串号
     */
    public static void loginOut(String unRegisterUrl, String applicationName) {
        Token token = IdentityProxy.getInstance().getToken();
        if (token == null) return;
        WSONet.<String>delete(unRegisterUrl +
                              "?applicationName=" +applicationName)
                .headers("User-Agent", "Mozilla/5.0 ( compatible ), Android")
                .headers("Accept", "*/*")
                .headers("Authorization", "Bearer " + token.getAccessToken())
                .headers("Content-Type", "application/json")
                .execute(new AuthCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        WSOLog.d("loginOut", "onSuccess:" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        WSOLog.d("loginOut", "onError:" + response.body());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        clearData(true);
                    }

                    @Override
                    public void netWorkError(String msg) {
                        super.netWorkError(msg);
                        clearData(true);
                    }

                }, false);
    }

    /**
     *
     * @param url
     * @param clientId
     * @param clientSecret
     * @param userName
     * @param passWord
     * @param scope
     * @param body
     * @param registerListener
     */
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
                .execute(new AuthCallback<TokenResponse>() {
                    @Override
                    public void onSuccess(Response<TokenResponse> response) {
                        if (response.isSuccessful()&&(response.code()==201||response.code()==200)) {
                            tokenResponse(response, false);
                            if (registerListener != null) {
                                TokenResponse body1 = response.body();
                                if (body1 != null) {
                                    registerListener.onSuccess(body, body1, response.code());
                                } else {
                                    registerListener.onFailure(response.message(), response.code());
                                }
                            }
                        } else {
                            if (registerListener != null) {
                                registerListener.onFailure(response.message(), response.code());
                            }
                        }

                    }

                    @Override
                    public void onError(Response<TokenResponse> response) {
                        super.onError(response);
                        IdentityProxy.getInstance()
                                     .receiveNewAccessToken(response.code(), response.message(), null);
                    }
                }, false);
    }

    /**
     * 刷新token
     *
     * @param apiAccessCallBack
     * @param url
     * @param clientID
     * @param clientSecret
     * @param token
     */
    public void refreshToken(final APIAccessCallBack apiAccessCallBack,
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
                    .execute(new AuthCallback<TokenResponse>() {
                        @Override
                        public void onSuccess(Response<TokenResponse> response) {
                            if (response.isSuccessful()&&(response.code()==201||response.code()==200)) {
                                tokenResponse(response, true);
                                if (apiAccessCallBack != null) {
                                    apiAccessCallBack.onAPIAccessReceive(Constant.Success, response.code(),
                                                                         IdentityProxy.getInstance()
                                                                                      .getToken());
                                }
                            } else {
                                tryRegister(response);
                            }
                        }

                        @Override
                        public void onError(Response<TokenResponse> response) {
                            super.onError(response);
                            tryRegister(response);
                        }

                        private void tryRegister(Response response) {
                            RegisterStore
                                    registerStore =
                                    new RegisterStore(WSONet.getInstance().getContext());
                            Register register = registerStore.getRegister();
                            if (register == null) {
                                clearData(false);
                                if (apiAccessCallBack != null) {
                                    apiAccessCallBack.onAPIAccessReceive(Constant.Failed, response.code(),
                                                                         IdentityProxy.getInstance()
                                                                                      .getToken());
                                }
                                return;
                            }
                            registerByRefreshtokenExpire(register.registerUrl,
                                                         register.tokenUrl,
                                                         register.registrationProfileRequestjson,
                                                         register.userName,
                                                         register.password,
                                                         register.scope,
                                                         new RegisterListener() {
                                                             @Override
                                                             public void onSuccess(RegisterResponse response,
                                                                                   TokenResponse tokenResponse,
                                                                                   int code) {
                                                                 callBackExcute(code);
                                                             }

                                                             @Override
                                                             public void onFailure(String resonseStr,
                                                                                   int code) {
                                                                 IdentityProxy.getInstance()
                                                                              .receiveNewAccessToken(code,
                                                                                                     resonseStr,
                                                                                                     null);
                                                                 callBackExcute(code);
                                                             }

                                                             @Override
                                                             public void netWorkError(String msg) {
                                                                 callBackExcute(-1);
                                                             }

                                                             public void callBackExcute(int code) {
                                                                 if (apiAccessCallBack != null) {
                                                                     apiAccessCallBack.onAPIAccessReceive(
                                                                             Constant.Success, code,
                                                                             IdentityProxy.getInstance()
                                                                                          .getToken());
                                                                 }
                                                             }
                                                         });
                        }
                    }, false);
        } catch (Exception e) {
            e.printStackTrace();
            if (apiAccessCallBack != null) {
                apiAccessCallBack.onAPIAccessReceive(Constant.Success, -1,
                                                     IdentityProxy.getInstance()
                                                                  .getToken());
            }
        }
    }


    /**
     *
     * @param response
     * @param isRefresh
     */
    public void tokenResponse(Response<TokenResponse> response, boolean isRefresh) {
        try{
            TokenResponse body = response.body();
            if(body==null||body.getExpires_in()==null)return ;
            TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
            Token token = new Token();
            String expireDate = TokenUtils.dateFormat.format(new Date().getTime() +
                                                             (Integer.parseInt(body.getExpires_in()) *
                                                              1000));
            token.setDate(expireDate);
            token.setRefreshToken(body.getRefresh_token());
            token.setAccessToken(body.getAccess_token());
            token.setExpired(false);
            token.setIdToken(body.getId_token());
            tokenStore.saveToken(token);
            WSOLog.d(TAG,"token :"+token.toString());
            if (isRefresh) {
                IdentityProxy.getInstance().receiveNewAccessToken(response.code(), response.message(), token);
            } else {
                IdentityProxy.getInstance().receiveNewAccessToken(response.code(), response.message(), token);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String base64(String obj1, String obj2) {
        if (obj1 != null && obj2 != null) {
            return Base64Utils.encode((obj1 + ":" + obj2).getBytes());
        }
        return null;
    }

    /**
     * 清除身份认证相关的数据
     *
     * @param isClearRegister
     */
    public static void clearData(boolean isClearRegister) {
        WSOLog.d(TAG, "clear all the token from memery and file ");
        IdentityProxy.getInstance().clearToken();
        if (isClearRegister) {
            WSOLog.d(TAG, "clear all the register info from file ");
            RegisterStore registerStore = new RegisterStore(WSONet.getInstance().getContext());
            registerStore.clearAll();
        }
    }

}
