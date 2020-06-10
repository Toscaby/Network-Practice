package com.tosca.networkpractice;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Tosca
 * @create at 17:41, 2020/6/10
 */
public class NetworkUtils {
  /**
   * 判断当前是否移动网络
   */
  public static boolean isMobile(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
    if (activeNetworkInfo != null
        && activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
      return true;
    }
    return false;
  }

  /**
   * 判断当前是否WIFI网络
   */
  public static boolean isWifi(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
    if (activeNetworkInfo != null
        && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
      return true;
    }
    return false;
  }

  /**
   * 是否2G网络
   */
  public static boolean is2G(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
    if (activeNetworkInfo != null
        && (activeNetworkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
        || activeNetworkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
        || activeNetworkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
      return true;
    }
    return false;
  }

  /**
   * 是否有网络连接
   */
  public static boolean isNetworkConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
    if (activeNetworkInfo != null) {
      return activeNetworkInfo.isAvailable();
    }
    return false;
  }

  /**
   * 返回网络类型
   * @return 4 - 4g / 3 - 3g / 2 - 2g / 1 - wifi / 0 - 无网络
   */
  public static int getAPNType(Context context) {
    int networkType = 0;

    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

    if (activeNetworkInfo == null) {
      // 没有网络
      return networkType;
    }

    int activeNetworkType = activeNetworkInfo.getType();
    switch (activeNetworkType) {
      case ConnectivityManager.TYPE_WIFI:
        networkType = 1;
        break;
      case ConnectivityManager.TYPE_MOBILE:
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int subType = activeNetworkInfo.getSubtype();
        // 4G
        if (subType == TelephonyManager.NETWORK_TYPE_LTE && !tm.isNetworkRoaming()) {
          networkType = 4;
          // 3G
        } else if ((subType == TelephonyManager.NETWORK_TYPE_UMTS
            || subType == TelephonyManager.NETWORK_TYPE_HSDPA
            || subType == TelephonyManager.NETWORK_TYPE_EVDO_0)
            && !tm.isNetworkRoaming()) {
          networkType = 3;
          // 2G
        } else if ((subType == TelephonyManager.NETWORK_TYPE_GPRS
            || subType == TelephonyManager.NETWORK_TYPE_EDGE
            || subType == TelephonyManager.NETWORK_TYPE_CDMA)
            && !tm.isNetworkRoaming()) {
          networkType = 2;
        } else {
          networkType = 2;
        }
        break;
    }
    return networkType;
  }

  /**
   * 是否打开GPS
   */
  public static boolean isGPSEnabled(Context context) {
    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  /**
   * 获取IP
   */
  public static String getIP() {
    try {
      // 1. 遍历网络接口
      for (Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
          eni.hasMoreElements();) {
         NetworkInterface networkInterface = eni.nextElement();
         // 2. 遍历InetAddress
        for (Enumeration<InetAddress> eia = networkInterface.getInetAddresses();eia.hasMoreElements();) {
          InetAddress inetAddress = eia.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            return inetAddress.getHostAddress();
          }
        }
      }
    } catch (SocketException se) {
      se.getLocalizedMessage();
    }
    return "";
  }

  /**
   * 获取MAC地址
   */
  public static String getMAC() {
    try {
      // 1. 获取网络接口列表
      List<NetworkInterface>  networkInterfaces
          = Collections.list(NetworkInterface.getNetworkInterfaces());

      // 2. 遍历查找是wlan0的接口
      for (NetworkInterface nif : networkInterfaces) {
        if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

        // 获取mac byte数组
        byte[] macBytes = nif.getHardwareAddress();
        if (macBytes == null) {
          return "";
        }

        StringBuilder sb = new StringBuilder();
        // 把byte格式化为字符串
        for (byte b : macBytes) {
          sb.append(String.format("%02X:", b));
        }

        // 去掉最后一个:
        if (sb.length() > 0) {
          sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
      }

    } catch (SocketException se) {
      se.getLocalizedMessage();
    }
    return "";
  }
}
