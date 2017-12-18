package com.example.trubin23.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by trubin23 on 18.12.17.
 */

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean wifiConnected = false, mobileConnected = false;

        if (connectivityManager != null) {
            NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();

            if (activeInfo != null && activeInfo.isConnected()) {
                wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
                mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }

        Log.d(TAG, " wifiConnected: " + wifiConnected);
        Log.d(TAG, " mobileConnected: " + mobileConnected);
    }
}
