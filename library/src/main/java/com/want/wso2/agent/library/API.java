package com.want.wso2.agent.library;

import com.want.wso2.agent.library.bean.DeviceResponse;
import com.want.wso2.agent.library.bean.GetConfigResponse;
import com.want.wso2.agent.library.bean.RegisterResponse;
import com.want.wso2.agent.library.bean.TokenResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by wisn on 2017/8/9.
 */

public interface API {
    @Headers({"User-Agent:Mozilla/5.0 ( compatible ), Android" , "Content-Type:application/json", "Accept:application/json"})
    @POST("api-application-registration/register")
    Call<RegisterResponse> register(@Header("Authorization") String authorization, @Body RequestBody requestBody);

    @Headers({"Content-Type:application/x-www-form-urlencoded"})
    @POST("token")
    Call<TokenResponse> getToken(@Header("Authorization") String authorization, @Query("username") String username, @Query("password") String password, @Query("grant_type") String grant_type, @Query("scope") String scope);

    @Headers({"Content-Type:application/x-www-form-urlencoded"})
    @POST("token")
    Call<TokenResponse> refreshToken(@Header("Authorization") String authorization, @Query("refresh_token") String refresh_token, @Query("grant_type") String grant_type, @Query("scope") String scope);

    @Headers({"User-Agent:Mozilla/5.0 ( compatible ), Android","Content-Type:application/json", "Accept:application/json"})
    @GET("api/device-mgt/android/v1.0/configuration")
    Call<GetConfigResponse> getConfig(@Header("Authorization") String authorization);

    @Headers({"User-Agent:Mozilla/5.0 ( compatible ), Android","Content-Type:application/json", "Accept:*/*"})
    @DELETE("api-application-registration/unregister")
    Call<String> loginOut(@Header("Authorization") String authorization,@Query("applicationName") String applicationName);

    @Headers({"User-Agent:Mozilla/5.0 ( compatible ), Android","Content-Type:application/json", "Accept:application/json"})
    @POST("api/device-mgt/android/v1.0/devices")
    Call<DeviceResponse> upDeviceInfo(@Header("Authorization") String authorization, @Body RequestBody requestBody);

    @Headers({"User-Agent:Mozilla/5.0 ( compatible ), Android","Content-Type:application/json", "Accept:application/json"})
    @POST()
    Call<ResponseBody> executePost(@Url String url, @Body RequestBody requestBody);

    @Headers({"User-Agent:Mozilla/5.0 ( compatible ), Android","Content-Type:application/json", "Accept:application/json"})
    @GET()
    Call<ResponseBody> executePost(@Url String url, @Header("Authorization") String authorization);

}
