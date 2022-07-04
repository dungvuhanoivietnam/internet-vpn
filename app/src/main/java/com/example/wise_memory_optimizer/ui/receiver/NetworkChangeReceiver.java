package com.example.wise_memory_optimizer.ui.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wise_memory_optimizer.custom.BroadcastReceiver;
import com.example.wise_memory_optimizer.utils.NetworkUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtils.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            intent.putExtra("status",status);
        }
    }
}
