package want.com.authtest.backup;

import android.text.TextUtils;

import com.want.wso2.utils.DataUitls;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Network {
    public static class Config {
        private Config() {
            throw new AssertionError();
        }

        /**
         * 服务器url
         */
//        public static String Url = "http://wso2dev.hollywant.com:8280/";
//        public static String Url = "http://10.0.35.10:9763/";
        public static String Url = "http://10.0.33.249:8080/";
        /**
         * 设置超时时间
         */
        public static long TIMEOUT = 10000;
    }

    private static String Url = Config.Url;
    private static long TIMEOUT = Config.TIMEOUT;
    private static volatile Network mInstance;
    private API mApi;

    private Network() {}

    public static Network getInstance() {
        if (mInstance == null) {
            synchronized (Network.class) {
                if (mInstance == null) {
                    mInstance = new Network();
                }
            }
        }
        return mInstance;
    }

    public  API getApi() {
        if (mApi == null) {
            synchronized (Network.class) {
                if (mApi == null) {
                    Retrofit.Builder retrofit = new Retrofit.Builder()
                            //使用自定义的mGsonConverterFactory
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(Url)
                            //.build();
                            .client(getClient());
                    mApi = retrofit.build().create(API.class);
                }
            }

        }
        return mApi;
    }

    /**
     * 初始化 OkHttpClient
     *
     * @return
     */
    private static OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient().newBuilder();

        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 获取 Cookie
                Response resp = chain.proceed(chain.request());
                List<String> cookies = resp.headers("Set-Cookie");
                String cookieStr = "";
                if (cookies != null && cookies.size() > 0) {
                    for (int i = 0; i < cookies.size(); i++) {
                        cookieStr += cookies.get(i);
                    }
                    DataUitls.saveCookieId(cookieStr);
                }
                return resp;
            }
        });
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // 设置 Cookie
                String cookieStr = DataUitls.getCookieId();
                if (TextUtils.isEmpty(cookieStr)) {
                    return chain.proceed(chain.request().newBuilder().header("Cookie", cookieStr).build());
                }
                return chain.proceed(chain.request());
            }
        });
        client.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        client.writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        client.readTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        return client.build();
    }
}
