package want.com.authtest;

import android.app.Application;

import com.want.wso2.WSONet;
import com.want.wso2.cache.CacheEntity;
import com.want.wso2.cache.CacheMode;
import com.want.wso2.model.HttpHeaders;
import com.want.wso2.model.HttpParams;

/**
 * Created by wisn on 2017/8/22.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initNetWork();
    }
    public void initNetWork(){
        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");
        try {
            WSONet.getInstance()
                  .init(this)
                 .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
