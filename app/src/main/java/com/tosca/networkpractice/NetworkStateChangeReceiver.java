package com.tosca.networkpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author Tosca
 * @create at 19:55, 2020/6/10
 */
public class NetworkStateChangeReceiver extends BroadcastReceiver {
  private static final String TAG = "NetworkChangeReceiver";

  @Override public void onReceive(Context context, Intent intent) {
    // 1. 监听WIFI打开与关闭
    if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
      int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
      switch (wifiState) {
        case WifiManager.WIFI_STATE_DISABLED:
          Log.d(TAG, "WIFI DISABLED");
          break;
        case WifiManager.WIFI_STATE_DISABLING:
          break;
        case WifiManager.WIFI_STATE_ENABLING:
          break;
        case WifiManager.WIFI_STATE_ENABLED:
          Log.d(TAG, "WIFI ENABLED");
          break;
        default:
          break;
      }
    }


    // 2. 监听wifi是否连接到有效的路由
    if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
      // 获取NetworkInfo
      Parcelable parcelableExtra = intent
          .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
      if (parcelableExtra != null) {
        NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) { // connect to internet
          Log.d(TAG,"WIFI CONNECTED");
          // 获取新的IP
          // ...
        }
      }
    }

    // 3. 可以监听Wifi和移动网络的开关
    if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
      // 获取ConnectivityManager
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

      if (activeNetwork != null) { // connect to internet
        if (activeNetwork.isConnected() && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
          Log.d(TAG,"WIFI CONNECTED");
          // 获取新的IP
          // ...
        }
      }
    }
  }
}
