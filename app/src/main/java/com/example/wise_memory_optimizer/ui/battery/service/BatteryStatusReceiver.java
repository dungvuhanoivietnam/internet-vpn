package com.example.wise_memory_optimizer.ui.battery.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.wise_memory_optimizer.ui.battery.BatteryInfo;
import com.example.wise_memory_optimizer.ui.battery.BatteryPref;
import com.example.wise_memory_optimizer.ui.battery.HistoryPref;
import com.example.wise_memory_optimizer.ui.battery.notification.NotificationBattery;
import com.example.wise_memory_optimizer.utils.SharePreferenceUtils;
import com.example.wise_memory_optimizer.utils.Utils;

public class BatteryStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryStatusReceiver";
    BatteryInfo mBatteryInfo = new BatteryInfo();
    NotificationCompat.Builder mBuilder;
    Context mContext = null;
    NotificationManager mNotificationManager;

    public void onReceive(Context context, Intent intent) {
        int i;
        String action = intent.getAction();
        if (action.equals("android.intent.action.BATTERY_CHANGED")) {
            boolean z = false;
            this.mBatteryInfo.level = intent.getIntExtra("level", 0);
            this.mBatteryInfo.scale = intent.getIntExtra("scale", -1);
            this.mBatteryInfo.temperature = intent.getIntExtra("temperature", -1);
            this.mBatteryInfo.voltage = intent.getIntExtra("voltage", -1);
            this.mBatteryInfo.technology = intent.getStringExtra("technology");
            this.mBatteryInfo.status = intent.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
            if (SharePreferenceUtils.getInstance(context).getLevelIn() == 0) {
                Utils.Companion.intPowerConnected(context);
                Utils.Companion.intSound(context);
            }
            if (this.mBatteryInfo.status == 5) {
                Utils.Companion.fullPower(context);
            }
            Utils.Companion.saveModeLowBattery(context);
            boolean z2 = this.mBatteryInfo.status == 2 || this.mBatteryInfo.status == 5;
            if (z2) {
                this.mBatteryInfo.plugged = intent.getIntExtra("plugged", -1);
                if (this.mBatteryInfo.plugged == 1) {
                    z = true;
                }
                if (z) {
                    i = BatteryPref.initilaze(context).getTimeChargingAc(this.mContext, this.mBatteryInfo.level);
                } else {
                    i = BatteryPref.initilaze(context).getTimeChargingUsb(this.mContext, this.mBatteryInfo.level);
                }
                this.mBatteryInfo.hourleft = i / 60;
                this.mBatteryInfo.minleft = i % 60;
            } else {
                int timeRemainning = BatteryPref.initilaze(context).getTimeRemainning(this.mContext, this.mBatteryInfo.level);
                this.mBatteryInfo.hourleft = timeRemainning / 60;
                this.mBatteryInfo.minleft = timeRemainning % 60;
            }
            intent.setAction(BatteryService.ACTION_BATTERY_CHANGED_SEND);
            intent.putExtra(BatteryInfo.BATTERY_INFO_KEY, this.mBatteryInfo);
            this.mContext.sendBroadcast(intent);
            NotificationBattery.getInstance(this.mContext).updateNotify(this.mBatteryInfo.level, this.mBatteryInfo.temperature, this.mBatteryInfo.hourleft, this.mBatteryInfo.minleft, z2);
        } else if (action.equals(BatteryService.ACTION_BATTERY_NEED_UPDATE)) {
            intent.setAction(BatteryService.ACTION_BATTERY_CHANGED_SEND);
            intent.putExtra(BatteryInfo.BATTERY_INFO_KEY, this.mBatteryInfo);
            this.mContext.sendBroadcast(intent);
            Log.e("batteryInfo", "batteryInfo: " + this.mBatteryInfo);
        } else if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            Utils.Companion.intPowerConnected(context);
        } else if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            Utils.Companion.powerDisconnected(context);
        }
        HistoryPref.putLevel(context, this.mBatteryInfo.level);
    }

    public String formatHourMinutune(long j) {
        String str;
        String str2;
        long j2 = (j / 1000) % 60;
        long j3 = (j / 60000) % 60;
        long j4 = j / 3600000;
        StringBuilder sb = new StringBuilder();
        if (j4 == 0) {
            str = "00";
        } else if (j4 < 10) {
            str = String.valueOf("0" + j4);
        } else {
            str = String.valueOf(j4);
        }
        sb.append(str);
        sb.append(":");
        if (j3 == 0) {
            str2 = "00";
        } else if (j3 < 10) {
            str2 = String.valueOf("0" + j3);
        } else {
            str2 = String.valueOf(j3);
        }
        sb.append(str2);
        return sb.toString();
    }

    public static String formatHoursAndMinutes(long j) {
        String l = Long.toString(j % 60);
        if (l.length() == 1) {
            l = "0" + l;
        }
        return (j / 60) + ":" + l;
    }

    public final void OnCreate(Context context) {
        this.mContext = context.getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction(BatteryService.ACTION_BATTERY_NEED_UPDATE);
        this.mContext.registerReceiver(this, intentFilter);
    }

    public final void OnDestroy(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }
}
