package com.example.testc2.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceUtil {

    public static final String DEVICE_UTIL_KEY="DeviceUtil";

    private static String wiredMac = "";

    public static String getMacAddress() {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase("eth0")) {
                    continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac != null) {
                    StringBuilder buf = new StringBuilder();
                    for (int idx = 0; idx < mac.length; idx++) {
                        buf.append(String.format("%02X:", mac[idx]));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    wiredMac =  buf.toString();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return wiredMac;
    }





    private static String wifiMac = "";

    public static String getWifiMac(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < 23) {
                WifiInfo info = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
                if (info != null) {
                    wifiMac = info.getMacAddress();
                }
            } else {
                try {
                    byte[] hwAddr = NetworkInterface.getByName("wlan0").getHardwareAddress();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < hwAddr.length; ++i) {
                        sb.append(String.format("%02X%s", hwAddr[i], i < hwAddr.length - 1 ? ":" : ""));
                    }
                    wifiMac = sb.toString();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } else{
            Log.d(DeviceUtil.DEVICE_UTIL_KEY,"no ACCESS_WIFI_STATE");
        }
        return wifiMac;
    }




}
