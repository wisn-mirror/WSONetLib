package com.want.wso2.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.want.wso2.bean.Token;
import com.want.wso2.utils.Constant;
import com.want.wso2.utils.TokenUtils;

/**
 * Created by wisn on 2017/8/23.
 */

public class TokenStore {
    private final SharedPreferences mSharedPreferences;

    public TokenStore(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constant.Token_Store, Context.MODE_PRIVATE);
    }

    public void saveToken(Token token) {
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.putString(Constant.Token_Accesstoken, token.getAccessToken());
        prefsWriter.putString(Constant.Token_refreshtoken, token.getRefreshToken());
        prefsWriter.putString(Constant.Token_date, TokenUtils.dateFormat.format(token.getDate()));
        prefsWriter.apply();
    }

    public Token getToken() {
        String Token_Accesstoken = mSharedPreferences.getString(Constant.Token_Accesstoken, "");
        String Token_refreshtoken = mSharedPreferences.getString(Constant.Token_refreshtoken, "");
        String Token_date = mSharedPreferences.getString(Constant.Token_date, "");
        if(!TextUtils.isEmpty(Token_Accesstoken)
           &&!TextUtils.isEmpty(Token_refreshtoken)
           &&!TextUtils.isEmpty(Token_date)){
            Token token = new Token();
            token.setDate(Token_date);
            token.setRefreshToken(Token_refreshtoken);
            token.setAccessToken(Token_Accesstoken);
            token.setExpired(false);
            return token;
        }else{
            return null;
        }
    }

    public void saveIdSecrect(String clientId, String clientSecrect) {
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.putString(Constant.clientId, clientId);
        prefsWriter.putString(Constant.clientSecrect, clientSecrect);
        prefsWriter.apply();
    }

    public void saveTokenUrl(String url) {
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.putString(Constant.tokenUrl, url);
        prefsWriter.apply();
    }

    public String getTokenUrl() {
        return mSharedPreferences.getString(Constant.tokenUrl, "");
    }

    public String getClientId() {
        return mSharedPreferences.getString(Constant.clientId, "");
    }

    public String getSecrect() {
        return mSharedPreferences.getString(Constant.clientSecrect, "");
    }

    public void clearAll() {
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.remove(Constant.clientId);
        prefsWriter.remove(Constant.clientSecrect);
        prefsWriter.remove(Constant.Token_Accesstoken);
        prefsWriter.remove(Constant.Token_refreshtoken);
        prefsWriter.remove(Constant.Token_date);
        prefsWriter.apply();
    }

}
