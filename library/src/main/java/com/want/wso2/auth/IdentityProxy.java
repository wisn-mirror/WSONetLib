package com.want.wso2.auth;

import android.content.Context;
import android.util.Log;

import com.want.wso2.WSONet;
import com.want.wso2.adapter.Call;
import com.want.wso2.bean.Token;
import com.want.wso2.callback.Callback;
import com.want.wso2.utils.TokenUtils;
import com.want.wso2.utils.WSOLog;

/**
 * Created by wisn on 2017/8/21.
 */

public class IdentityProxy implements AuthenticatorCallBack {
    private static final String TAG = "IdentityProxy";
    private static Token token = null;
    private static IdentityProxy identityProxy = new IdentityProxy();
    private Context context;
    private APIAccessCallBack apiAccessCallBack;

    private IdentityProxy() {}

    public static synchronized IdentityProxy getInstance() {
        return identityProxy;
    }

    public Token getToken() {
        return token;
    }

    public void checkToken(APIAccessCallBack apiAccessCallBack, Call call, Callback callback) {
        this.apiAccessCallBack = apiAccessCallBack;
        if (token == null) {
            TokenStore TokenStore = new TokenStore(WSONet.getInstance().getContext());
            this.token = TokenStore.getToken();
            if (this.token == null) {
                this.apiAccessCallBack.onAPIAccessReceive("token is null", null);
                if (call != null && callback != null) {
                    call.execute(callback);
                }
            } else {
                validateStoredToken(call, callback);
            }
        } else {
            validateStoredToken(call, callback);
        }
    }

    public Token isLogin(){
        if (token == null) {
            TokenStore TokenStore = new TokenStore(WSONet.getInstance().getContext());
            this.token = TokenStore.getToken();
        }
        return token;
    }

    private void validateStoredToken(Call call, Callback callback) {
        boolean isExpired = TokenUtils.isValid(token.getDate());
        if (!isExpired) {
            WSOLog.d(TAG, "stored token is not expired.");
            synchronized (this) {
                this.apiAccessCallBack.onAPIAccessReceive("success", token);
                if (call != null && callback != null) {
                    call.execute(callback);
                }
            }
        } else {
            Log.d(TAG, "stored token is expired, refreshing");
            refreshToken(call, callback);
        }
    }

    public void clearToken() {
        token = null;
    }

    private void refreshToken(Call call, Callback callback) {
        TokenStore TokenStore = new TokenStore(WSONet.getInstance().getContext());
        Authenticator Authenticator = new Authenticator();
        Authenticator.refreshToken(call, callback, TokenStore.getTokenUrl(),
                                   TokenStore.getClientId(),
                                   TokenStore.getSecrect(),
                                   token);
    }

    @Override
    public void receiveAccessToken(int code, String message, Token token) {
        if (code == 200) {
            IdentityProxy.token = token;
            if (this.apiAccessCallBack != null)
                this.apiAccessCallBack.onAPIAccessReceive("success", token);
        }
    }

    @Override
    public void receiveNewAccessToken(int code, String message, Token token) {
        if (code == 200) {
            IdentityProxy.token = token;
            if (this.apiAccessCallBack != null)
                this.apiAccessCallBack.onAPIAccessReceive("success", token);
        }
    }
}
