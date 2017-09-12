package want.com.authtest.backup;

import com.want.wso2.bean.DeviceRequest;
import com.want.wso2.bean.DeviceResponse;
import com.want.wso2.bean.GetConfigResponse;
import com.want.wso2.bean.RegisterResponse;
import com.want.wso2.bean.RegistrationProfileRequest;
import com.want.wso2.bean.TokenRequest;
import com.want.wso2.bean.TokenResponse;

import org.apache.commons.codec.binary.Base64;

import okhttp3.RequestBody;
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

