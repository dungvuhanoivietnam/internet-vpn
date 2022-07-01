package com.example.wise_memory_optimizer.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkUtils {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isConnectVpn() {
        List<String> networkList = new ArrayList<>();
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    networkList.add(networkInterface.getName());
            }
        } catch (Exception ex) {

        }

        return networkList.contains("tun0");
    }
}
