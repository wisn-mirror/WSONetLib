package com.want.wso2.auth;


import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.want.wso2.bean.BaseNetBean;
import com.want.wso2.bean.DeviceRequest;
import com.want.wso2.bean.DeviceResponse;
import com.want.wso2.bean.GetConfigResponse;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenRequest;
import com.want.wso2.bean.TokenResponse;
import com.want.wso2.interfaces.NetListener;
import com.want.wso2.utils.JsonTool;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wisn on 2017/8/9.
 */

public class APIController {
    private static final String TAG="APIController";

    public Call<RegisterResponse> register(RegistrationProfileRequest registrationProfileRequest,
                                           String userName,
                                           String password,
                                           final Callback<RegisterResponse> callback) {
        String basicAuthValue = "Basic " + base64(userName, password);
        API api = Network.getInstance().getApi();
        RequestBody
                body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                                   registrationProfileRequest.toJSON());
        Call<RegisterResponse> call = api.register(basicAuthValue, body);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
                //Todo 获取token
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }
        });
        return call;
    }


    public Call<TokenResponse> getToken(TokenRequest tokenRequest,
                                        Callback<TokenResponse> callback) {
        String basicAuthValue = "Basic " + base64(tokenRequest.getClientID(), tokenRequest
                .getClientSecret());
        API api = Network.getInstance().getApi();
        Call<TokenResponse>
                call =
                api.getToken(basicAuthValue,
                             tokenRequest.getUsername(),
                             tokenRequest.getPassword(),
                             "password",
                             tokenRequest.getScope());
        call.enqueue(callback);
        return call;
    }

    public Call<TokenResponse> refreshToken(TokenRequest tokenRequest, String refresh_token,
                                            Callback<TokenResponse> callback) {
        String basicAuthValue = "Basic " + base64(tokenRequest.getClientID(), tokenRequest
                .getClientSecret());
        API api = Network.getInstance().getApi();
        Call<TokenResponse>
                call =
                api.refreshToken(basicAuthValue, refresh_token, "refresh_token", "PRODUCTION");
        call.enqueue(callback);
        return call;
    }

    public <T> Call excutePost(String api_url,
                               Object requestParam,
                               final Class classz,
                               final NetListener<T> netListener) {
        API api = Network.getInstance().getApi();
        RequestBody
                body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                                   JsonTool.toJson(requestParam));
        Call<ResponseBody> responseBodyCall = api.executePost(api_url, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String responseStr= null;
                    int code=200;
                    String msg=null;
                    T t= null;
                    try {
                        responseStr = response.body().string();
                        JSONObject object = new JSONObject(responseStr);
                        code=object.getInt("code");
                        msg=object.getString("msg");
                        String data=object.getString("data");
                        Log.e(TAG,data);
//                        t =JsonTool.getInstance().fromJson(data, new TypeToken<T>() {}.getType());
                        t = (T) JsonTool.getInstance().fromJson(data, classz );
                        Log.e(TAG,responseStr);
                        BaseNetBean<T> baseNetBean1 =JsonTool.getInstance().fromJson(responseStr, new TypeToken<BaseNetBean<T>>() {}.getType());
                        Log.e(TAG,"fdasf "+baseNetBean1.data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg=e.getMessage();
                    }catch (JSONException e){
                        e.printStackTrace();
                        msg=e.getMessage();
                    }finally {
                        //todo code message 约定数据格式处理
                        netListener.onSuccess(t,code,msg,responseStr);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //todo 错误消息式处理
                netListener.onFailure(t,"message ");
            }
        });
        return responseBodyCall;
    }

    public <T> Call<T> sendJsonRequestGet(String api_url, String accessToken, Class<T> tClass,
                                          Callback<T> callback) {
//        String basicAuthValue = "Bearer " + accessToken;
//        API api = Network.getInstance().getApi();
//        Call<T> tCall = api.sendJsonRequestGet(api_url, basicAuthValue, tClass);
//        tCall.enqueue(callback);
        return null;
    }


    public Call<GetConfigResponse> getConfig(String accessToken,
                                             Callback<GetConfigResponse> callback) {
        String basicAuthValue = "Bearer " + accessToken;
        API api = Network.getInstance().getApi();
        Call<GetConfigResponse> call = api.getConfig(basicAuthValue);
        call.enqueue(callback);
        return call;
    }

    public Call<String> loginOut(String accessToken, String applicationName,
                                 Callback<String> callback) {
        String basicAuthValue = "Bearer " + accessToken;
        API api = Network.getInstance().getApi();
        Call<String> call = api.loginOut(basicAuthValue, applicationName);
        call.enqueue(callback);
        return call;
    }


    public Call<DeviceResponse> upDeviceInfo(DeviceRequest deviceRequest, String accessToken,
                                             Callback<DeviceResponse> callback) {
        String basicAuthValue = "Bearer " + accessToken;
        API api = Network.getInstance().getApi();
        RequestBody
                body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                                   deviceRequest.toJSON());
        Call<DeviceResponse> call = api.upDeviceInfo(basicAuthValue, body);
        call.enqueue(callback);
        return call;
    }


    public String base64(String obj1, String obj2) {
        if (obj1 != null && obj2 != null) {
            return new String(Base64.encodeBase64((obj1 + ":" + obj2).getBytes()));
        }
        return null;
    }

}

