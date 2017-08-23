package com.want.wso2.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.want.wso2.Constant;
import com.want.wso2.bean.Token;
import com.want.wso2.cookie.SerializableCookie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by wisn on 2017/8/23.
 */

public class TokenStore {
    public static final DateFormat dateFormat =
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
    private final SharedPreferences mSharedPreferences;

    public TokenStore(Context context){
        mSharedPreferences = context.getSharedPreferences(Constant.Token_Store, Context.MODE_PRIVATE);
    }
    public void saveToken(Token token){
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.putString(Constant.Token_Accesstoken,token.getAccessToken());
        prefsWriter.putString(Constant.Token_refreshtoken,token.getRefreshToken());
        prefsWriter.putString(Constant.Token_date,dateFormat.format(token.getDate()));
        prefsWriter.apply();
    }
    public void getToken(){

    }
}
