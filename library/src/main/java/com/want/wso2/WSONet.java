package com.want.wso2;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.want.wso2.cache.CacheEntity;
import com.want.wso2.cache.CacheMode;
import com.want.wso2.cookie.CookieJarImpl;
import com.want.wso2.https.HttpsUtils;
import com.want.wso2.interceptor.HttpLoggingInterceptor;
import com.want.wso2.model.HttpHeaders;
import com.want.wso2.model.HttpParams;
import com.want.wso2.request.DeleteRequest;
import com.want.wso2.request.GetRequest;
import com.want.wso2.request.HeadRequest;
import com.want.wso2.request.OptionsRequest;
import com.want.wso2.request.PatchRequest;
import com.want.wso2.request.PostRequest;
import com.want.wso2.request.PutRequest;
import com.want.wso2.request.TraceRequest;
import com.want.wso2.utils.DeviceUtils;
import com.want.wso2.utils.HttpUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by wisn on 2017/8/22.
 */
public class WSONet {


    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Application context;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    /**
     * init config
     */
    private WSONet() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.NO_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("WSONet");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(WSONet.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(WSONet.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(WSONet.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
    }

    private void addPublicData() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("imei", DeviceUtils.getDeviceId(this.getContext()));
        headers.put("appname", DeviceUtils.getAppName(this.getContext()));
        HttpParams params = new HttpParams();
        params.put("app_version", DeviceUtils.getAppVersion(this.getContext()));
        params.put("app_code", DeviceUtils.getAppCode(this.getContext()));
        addCommonParams(params);
        addCommonHeaders(headers);
    }

    public static WSONet getInstance() {
        return WSONetHolder.holder;
    }

    private static class WSONetHolder {
        private static WSONet holder = new WSONet();
    }

    /**
     * init WSONet on Application
     *
     * @param app
     *
     * @return
     */
    public WSONet init(Application app) {
        context = app;
        addPublicData();
        return this;
    }

    /**
     * set retryCount
     *
     * @param retryCount
     *
     * @return
     */
    public WSONet setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /**
     * set CacheMode
     *
     * @param cacheMode
     *
     * @return
     */
    public WSONet setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /**
     * set Cache timeOut time
     *
     * @param cacheTime
     *
     * @return
     */
    public WSONet setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /**
     * add public params
     *
     * @param commonParams
     *
     * @return
     */
    public WSONet addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /**
     * add public header
     *
     * @param commonHeaders
     *
     * @return
     */
    public WSONet addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /**
     * setOkHttpClient
     *
     * @param okHttpClient
     *
     * @return
     */
    public WSONet setOkHttpClient(OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * get request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    /**
     * post request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }

    /**
     * put request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    /**
     * head request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    /**
     * delete request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    /**
     * options request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    /**
     * patch request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    /**
     * trace request
     *
     * @param url
     * @param <T>
     *
     * @return
     */
    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }


    /**
     * getContext
     *
     * @return
     */
    public Context getContext() {
        HttpUtils.checkNotNull(context, "please call WSONet.getInstance().init() first in application!");
        return context;
    }

    /**
     * @return
     */
    public Handler getDelivery() {
        return mDelivery;
    }

    /**
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient,
                               "please call WSONet.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /**
     * get cookie instance
     *
     * @return
     */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }


    /**
     * get retryCount
     *
     * @return
     */
    public int getRetryCount() {
        return mRetryCount;
    }


    /**
     * get CacheMode
     *
     * @return
     */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }


    /**
     * get Cache timeOut time
     *
     * @return
     */
    public long getCacheTime() {
        return mCacheTime;
    }

    /**
     * get  public params
     *
     * @return
     */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /**
     * get public header
     *
     * @return
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }


    /**
     * cancel request by tag
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * cancel request by tag
     *
     * @param client
     * @param tag
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * cancel all request
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * cancel all request
     *
     * @param client
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
