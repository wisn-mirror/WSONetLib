package want.com.authtest.backup;

import android.util.Log;

/**
 * Created by wisn on 2017/8/21.
 */

public class DataUitls {
    public final static String TAG="DataUitls";
    public static  void saveCookieId(String cookieId){
        Log.e(TAG," cookieId:"+cookieId);
    }
    public static String  getCookieId(){
        Log.e(TAG," get cookieId");

        return "";
    }
}
