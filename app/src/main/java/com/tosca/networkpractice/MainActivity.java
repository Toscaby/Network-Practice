package com.tosca.networkpractice;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
  private NetworkStateChangeReceiver networkStateChangeReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    networkStateChangeReceiver = new NetworkStateChangeReceiver();
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
    intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    registerReceiver(networkStateChangeReceiver, intentFilter);
  }
}
