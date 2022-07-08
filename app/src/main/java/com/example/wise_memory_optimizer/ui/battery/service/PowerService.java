package com.example.wise_memory_optimizer.ui.battery.service;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.wise_memory_optimizer.MainActivity;
import com.example.wise_memory_optimizer.MyApplication;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.custom.QiscusNumberUtil;
import com.example.wise_memory_optimizer.utils.Const;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PowerService extends Service {
    public PowerConnectionReceiver mPowconnectReceicever;
    public static String NOTI_CHANNEL_ID = "noti_my_service";
    static RemoteViews remoteViews;

    private void addBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(this.mPowconnectReceicever, intentFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification initNotificationAndroidO() {
        ((NotificationManager) getSystemService(NotificationManager.class))
                .createNotificationChannel(new NotificationChannel(NOTI_CHANNEL_ID, "My Background Service", NotificationManager.IMPORTANCE_DEFAULT));
        return new NotificationCompat.Builder(this, NOTI_CHANNEL_ID).getNotification();
    }

    private void startServiceForeground() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class))
                    .createNotificationChannel(new NotificationChannel(NOTI_CHANNEL_ID, "My Background Service", NotificationManager.IMPORTANCE_NONE));

            remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_optimize);
//            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_remote_view);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Const.EXTRA_NOTIFICATION, Const.EXTRA_NOTIFICATION);
            intent.setAction(Const.OPTIMIZE_NOTIFIED);
            intent.setFlags(603979776);

            int i = HawkHelper.isChargerConnected() ? R.string.app_name : R.string.app_name;
            NotificationCompat.Builder cVar = new NotificationCompat.Builder(this, NOTI_CHANNEL_ID);
            cVar.setSmallIcon((int) R.mipmap.ic_launcher);
            cVar.setAutoCancel(true);
            cVar.setCustomContentView(remoteViews);
            PendingIntent activity;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity = PendingIntent.getBroadcast(this, QiscusNumberUtil.convertToInt(0),
                        intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
            } else {
                activity = PendingIntent.getBroadcast(this, QiscusNumberUtil.convertToInt(0),
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);
            }

            cVar.setContentIntent(activity);
            int useRam = (int) (getTotalRAM() - getAvaiableRam(this));
            remoteViews.setTextViewText(R.id.txt_ram, (Double.valueOf(useRam) / Double.valueOf(getTotalRAM()) * 100) + "%");
            remoteViews.setTextViewText(R.id.txt_temp, MyApplication.Companion.getItem());
            remoteViews.setTextViewText(R.id.txt_batt_percent, MyApplication.Companion.getBattery_level());
            remoteViews.setOnClickPendingIntent(R.id.view, activity);
            try {
                startForeground(103, cVar.getNotification());
            } catch (Exception exception) {
//                LogUtil.e("#PowerService - startServiceForeground exception - " + exception.getMessage());
            }
        }
    }

    public static void setMBS(Float mbs) {
//        remoteViews.setTextViewText(R.id.txt_global, mbs + "Kb/s");
    }

    private Long getAvaiableRam(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    private Long totalRAM;

    public Long getTotalRAM() {
        RandomAccessFile reder = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        long totRam = 0L;
        try {
            reder = new RandomAccessFile("/proc/meminfo", "r");
            load = reder.readLine();

            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
            }
            reder.close();
            totRam = Integer.valueOf(value);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return totRam * 1024;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
//        LogUtil.e("#PowerService - onCreate()");
        this.mPowconnectReceicever = new PowerConnectionReceiver();
        startServiceForeground();
        addBroadcastReceiver();

//        LogUtil.e("#PowerService - onCreate()");
    }

    public void onDestroy() {
        super.onDestroy();
        PowerConnectionReceiver powerConnectionReceiver = this.mPowconnectReceicever;
        if (powerConnectionReceiver != null) {
            unregisterReceiver(powerConnectionReceiver);
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
//        LogUtil.e("#PowerService - onStartCommand()");
        return START_STICKY;
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
//        Intent intent2 = new Intent(getApplicationContext(), FloatActivity.class);
//        intent2.setPackage(getPackageName());
//        ((AlarmManager) getApplicationContext().getSystemService("alarm")).set(3, SystemClock.elapsedRealtime() + 1000, PendingIntent.getService(getApplicationContext(), 1, intent2, 1073741824));
    }

    public static void startMy(Context context) {
        Intent intent = new Intent(context, PowerService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
