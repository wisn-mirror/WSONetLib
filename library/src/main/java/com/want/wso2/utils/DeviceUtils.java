package com.want.wso2.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wisn on 2017/8/23.
 */

public class DeviceUtils {

    /**
     * Returns the network operator name.
     *
     * @return - Network operator name.
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null)
            return telephonyManager.getSimOperatorName();
        return "";
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * Returns the device model.
     *
     * @return - Device model.
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * Returns the device manufacturer.
     *
     * @return - Device manufacturer.
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }


    /**
     * Returns the OS version.
     *
     * @return - Device OS version.
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Returns the SDK Version number.
     *
     * @return - Device android SDK version number.
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

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
                deviceId = m.replaceAll(" ").replace(":", " ");
            }
        }
        return deviceId;
    }

    /**
     * Returns the IMEI Number.
     *
     * @return - Device IMEI number.
     */
    public static String getDeviceId(Context context) {
        String deviceId = null;
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            deviceId = telephonyManager.getDeviceId();
        }
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        Pattern p = Pattern.compile("IMEI", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(deviceId);
        deviceId = m.replaceAll(" ");
        deviceId = deviceId.replace(":", " ");
        return deviceId.trim();
    }

    /**
     * get App versionName
     *
     * @return
     */
    public static String getAppVersion(Context context) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                                             .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * get app version code
     *
     * @return
     */
    public static int getAppCode(Context context) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                                             .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * get AppName
     *
     * @return
     */
    public static String getAppName(Context context) {
        if (context != null) {
            return context.getPackageName();
        }
        return "unKnow";
    }

    /**
     * Returns the IMSI Number.
     *
     * @return - Device IMSI number.
     */
    public static String getIMSINumber(Context context) {
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null)
            return telephonyManager.getSubscriberId();
        return "";
    }

    /**
     * Returns the device WiFi MAC.
     *
     * @return - Device WiFi MAC.
     */
    public static String getMACAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }


    /**
     * Returns the SIM serial number.
     *
     * @return - Device SIM serial number.
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager
                telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null)
            return telephonyManager.getSimSerialNumber();
        return "";
    }

    /**
     * Returns the hardware serial number.
     *
     * @return - Hardware serial number.
     */
    public static String getDeviceSerialNumber() {
        return Build.SERIAL;
    }

    /**
     * Returns all the sensors available on the device as a List.
     *
     * @return - List of all the sensors available on the device.
     */
    public static List<Sensor> getAllSensors(Context context) {
        SensorManager sensorManager =
                (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        return sensorManager.getSensorList(Sensor.TYPE_ALL);
    }


}
