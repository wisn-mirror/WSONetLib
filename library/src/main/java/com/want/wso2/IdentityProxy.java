package com.want.wso2;

import com.want.wso2.bean.Token;
import android.content.Context;

/**
 * Created by wisn on 2017/8/21.
 */

public class IdentityProxy {
    private static Token token = null;
    private static IdentityProxy identityProxy = new IdentityProxy();
    private Context context;

    private IdentityProxy() {}

    public static synchronized IdentityProxy getInstance() {
        return identityProxy;
    }

    private void validateStoredToken() {/*
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
                synchronized (this) {
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
            synchronized (this) {
                IdentityProxyBackup.getInstance().receiveNewAccessToken(Constants.ACCESS_FAILURE,
                                                                        Constants.FAILURE_RESPONSE, token);
            }
        }*/
    }
}
