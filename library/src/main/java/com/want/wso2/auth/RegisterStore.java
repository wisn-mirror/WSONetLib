package com.want.wso2.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.want.wso2.bean.Register;
import com.want.wso2.utils.Constant;

/**
 * Created by wisn on 2017/12/1.
 */

public class RegisterStore {

    private final SharedPreferences mSharedPreferences;

    public RegisterStore(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constant.Register_Store, Context.MODE_PRIVATE);
    }

    public void saveRegister(Register Register) {
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.putString(Constant.password, Register.password);
        prefsWriter.putString(Constant.UserName, Register.userName);
        prefsWriter.putString(Constant.scope, Register.scope);
        prefsWriter.putString(Constant.RegisterUrl, Register.registerUrl);
        prefsWriter.putString(Constant.tokenUrl, Register.tokenUrl);
        prefsWriter.putString(Constant.RegistrationProfileRequestjson,
                              Register.registrationProfileRequestjson);
        prefsWriter.apply();
    }

    public Register getRegister() {
        String password = mSharedPreferences.getString(Constant.password, "");
        String UserName = mSharedPreferences.getString(Constant.UserName, "");
        String scope = mSharedPreferences.getString(Constant.scope, "");
        String RegisterUrl = mSharedPreferences.getString(Constant.RegisterUrl, "");
        String tokenUrl = mSharedPreferences.getString(Constant.tokenUrl, "");
        String
                RegistrationProfileRequestjson =
                mSharedPreferences.getString(Constant.RegistrationProfileRequestjson, "");
        if (!TextUtils.isEmpty(password)
            && !TextUtils.isEmpty(UserName)
            && !TextUtils.isEmpty(scope)
            && !TextUtils.isEmpty(RegisterUrl)
            && !TextUtils.isEmpty(tokenUrl)
            && !TextUtils.isEmpty(RegistrationProfileRequestjson)) {
            Register
                    register =
                    new Register(RegisterUrl,
                                 tokenUrl,
                                 RegistrationProfileRequestjson,
                                 UserName,
                                 password,
                                 scope);
            return register;
        } else {
            return null;
        }
    }

    public void changePassword(String newPassword){
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.putString(Constant.password, newPassword);
        prefsWriter.apply();
    }

    public void clearAll() {
        SharedPreferences.Editor prefsWriter = mSharedPreferences.edit();
        prefsWriter.remove(Constant.password);
        prefsWriter.remove(Constant.UserName);
        prefsWriter.remove(Constant.scope);
        prefsWriter.remove(Constant.RegisterUrl);
        prefsWriter.remove(Constant.tokenUrl);
        prefsWriter.remove(Constant.RegistrationProfileRequestjson);
        prefsWriter.apply();
    }
}
