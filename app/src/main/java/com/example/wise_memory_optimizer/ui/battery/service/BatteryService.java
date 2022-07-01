package com.example.wise_memory_optimizer.ui.battery.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import androidx.core.internal.view.SupportMenu;

import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.ui.battery.notification.NotificationBattery;
import com.example.wise_memory_optimizer.utils.SharePreferenceUtils;
import com.example.wise_memory_optimizer.utils.Utils;

public class BatteryService extends Service {
    public static final String ACTION_BATTERY_CHANGED_SEND = "ACTION_BATTERY_CHANGED_SEND";
    public static final String ACTION_BATTERY_NEED_UPDATE = "ACTION_BATTERY_NEED_UPDATE";
    public static final String NOTIFY_HOME = "com.battery.main";
    BatteryStatusReceiver mBatteryStatusReceiver;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                SharePreferenceUtils.getInstance(context).getKillApp();
                AlarmUtils.cancel(context, AlarmUtils.ACTION_CHECK_DEVICE_STATUS);
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                SharePreferenceUtils.getInstance(context).setLevelScreenOn(Utils.Companion.getBatteryLevel(context));
                AlarmUtils.setAlarm(context, AlarmUtils.ACTION_CHECK_DEVICE_STATUS, 300000);
            }
        }
    };
//    TaskScreenOff mTaskScreenOff;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    public void onCreate() {
        super.onCreate();
        resScreen();
        this.mBatteryStatusReceiver = new BatteryStatusReceiver();
        this.mBatteryStatusReceiver.OnCreate(this);
        if (Build.VERSION.SDK_INT >= 26) {
            createChanelID();
        }
        startForeground(1, NotificationBattery.getInstance(this).build());
    }

    @SuppressLint({"WrongConstant"})
    @TargetApi(26)
    private void createChanelID() {
        try {
            String string = getString(R.string.app_name);
            String string2 = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel("my_channel_fast_charger", string, 2);
            notificationChannel.setDescription(string2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        } catch (Exception unused) {
        }
    }

    public void onTaskRemoved(Intent intent) {
        AlarmUtils.setAlarm(this, AlarmUtils.ACTION_REPEAT_SERVICE, 1000);
        super.onTaskRemoved(intent);
    }

    public void onDestroy() {
        super.onDestroy();
        AlarmUtils.setAlarm(this, AlarmUtils.ACTION_REPEAT_SERVICE, 1000);
        if (this.mBatteryStatusReceiver != null) {
            this.mBatteryStatusReceiver.OnDestroy(getApplicationContext());
            this.mBatteryStatusReceiver = null;
        }
//        cancleTask();
        try {
            unregisterReceiver(this.mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void cancleTask() {
//        if (this.mTaskScreenOff != null && this.mTaskScreenOff.getStatus() == AsyncTask.Status.RUNNING) {
//            this.mTaskScreenOff.cancel(true);
//            this.mTaskScreenOff = null;
//        }
//    }

    public void resScreen() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(this.mReceiver, intentFilter);
    }
}
