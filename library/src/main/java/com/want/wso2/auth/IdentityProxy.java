package com.want.wso2.auth;

import com.want.wso2.WSONet;
import com.want.wso2.bean.Register;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.Token;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.interfaces.RegisterListener;
import com.want.wso2.utils.Constant;
import com.want.wso2.utils.TokenUtils;
import com.want.wso2.utils.WSOLog;

/**
 * Created by wisn on 2017/8/21.
 */

public class IdentityProxy implements TokenCallBack {
    private static final String TAG = "IdentityProxy";
    private static Token token = null;
    private static IdentityProxy identityProxy = new IdentityProxy();

    private IdentityProxy() {}

    public static synchronized IdentityProxy getInstance() {
        return identityProxy;
    }

    public Token getToken() {
        return token;
    }

    public void checkToken(APIAccessCallBack apiAccessCallBack) {
        if (token == null) {
            TokenStore TokenStore = new TokenStore(WSONet.getInstance().getContext());
            this.token = TokenStore.getToken();
        }
        if (this.token == null) {
            tryRegister(apiAccessCallBack);
        } else {
            validateStoredToken(apiAccessCallBack);
        }
    }

    public Token isLogin() {
        if (token == null) {
            TokenStore TokenStore = new TokenStore(WSONet.getInstance().getContext());
            this.token = TokenStore.getToken();
        }
        return token;
    }

    private void validateStoredToken(APIAccessCallBack apiAccessCallBack) {
        boolean isExpired = TokenUtils.isValid(token.getDate());
        if (!isExpired) {
            WSOLog.d(TAG, "stored token is not expired.");
            synchronized (this) {
                apiAccessCallBack.onAPIAccessReceive("success",200, token);
            }
        } else {
            WSOLog.d(TAG, "stored token is expired, refreshing");
            refreshToken(apiAccessCallBack);
        }
    }

    public void clearToken() {
        token = null;
        TokenStore tokenStore = new TokenStore(WSONet.getInstance().getContext());
        tokenStore.clearAll();
    }

    /**
     * @param apiAccessCallBack
     */
    public void tryRegister(final APIAccessCallBack apiAccessCallBack) {
        RegisterStore
                registerStore =
                new RegisterStore(WSONet.getInstance().getContext());
        Register register = registerStore.getRegister();
        if (register == null) {
            if (apiAccessCallBack != null) {
                apiAccessCallBack.onAPIAccessReceive(Constant.Success,-1, this.getToken());
            }
            return;
        }
        Authenticator authenticator = new Authenticator();
        authenticator.registerByRefreshtokenExpire(register.registerUrl,
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
                                                                       Constant.Success,code,
                                                                       IdentityProxy.getInstance()
                                                                                    .getToken());
                                                           }
                                                       }
                                                   });
    }

    /**
     *
     * @param apiAccessCallBack
     */
    private void refreshToken(APIAccessCallBack apiAccessCallBack) {
        TokenStore TokenStore = new TokenStore(WSONet.getInstance().getContext());
        Authenticator Authenticator = new Authenticator();
        Authenticator.refreshToken(apiAccessCallBack, TokenStore.getTokenUrl(),
                                   TokenStore.getClientId(),
                                   TokenStore.getSecrect(),
                                   token);
    }

    /**
     *
     * @param code
     * @param message - Success/Error message.
     * @param token   - Token retrieved.
     */
    @Override
    public void receiveNewAccessToken(int code, String message, Token token) {
        if ((code == 200 || code == 201) || token != null) {
            IdentityProxy.token = token;
        }
        // TODO: 2017/12/3  token ==null 激活重新注册策略
    }
}
