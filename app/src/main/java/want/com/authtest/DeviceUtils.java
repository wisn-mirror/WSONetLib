package want.com.authtest;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wisn on 2017/8/9.
 */

public class DeviceUtils {

    /**
     * Returns the device name.
     *
     * @return - Device name.
     */
    public static String getDeviceName(Context context) {
        String deviceId = "";
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            deviceId = telephonyManager.getDeviceId();
            if (!TextUtils.isEmpty(deviceId)) {
                deviceId = deviceId.trim();
                Pattern p = Pattern.compile("IMEI", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(deviceId);
                deviceId = m.replaceAll(" ");
                deviceId = deviceId.replace(":", " ");
            }
        }
        return deviceId;
    }
}
