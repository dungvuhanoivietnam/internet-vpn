package com.example.wise_memory_optimizer.ui.battery.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.example.wise_memory_optimizer.MainActivity;
import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.utils.SharePreferenceUtils;
import com.example.wise_memory_optimizer.utils.Utils;

public class NotificationDevice {
    public static final int ID_NOTIFICATTION_BOOST = 2;
    public static final int ID_NOTIFICATTION_CLEAN_JUNK = 3;
    public static final int ID_NOTIFICATTION_COOLER = 5;
    public static final int ID_NOTIFICATTION_FULL_BATTERY = 6;
    public static final int ID_NOTIFICATTION_LOW_BATTERY = 7;
    public static final int ID_NOTIFICATTION_OPTIMIZE = 4;

//    public static void showNotificationTemp(Context context) {
//        try {
//            Intent intent = new Intent(context, CoolerActivity.class);
//            intent.setFlags(268435456);
//            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C0762R.layout.notification_cool_down);
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.cool_down_title_2) + " " + getTemp(C0769Utils.getTempleCpu(context), context));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.cool_down_title) + " " + getTemp(C0769Utils.getTempleCpu(context), context));
//            }
//            remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.cool_down_des));
//            remoteViews.setTextViewText(C0762R.C0764id.btnAction, context.getString(C0762R.string.cool_down_button));
//            NotificationCompat.Builder customContentView = new NotificationCompat.Builder(context).setSmallIcon(C0762R.C0763drawable.ic_noti_small_temp).setOngoing(true).setVibrate(new long[]{0}).setOnlyAlertOnce(true).setContentIntent(activity).setAutoCancel(true).setPriority(4).setCustomContentView(remoteViews);
//            if (C0769Utils.isAndroid26()) {
//                createChanelID(context);
//                customContentView.setChannelId("my_channel");
//            }
//            Notification build = customContentView.build();
//            build.flags = 20;
//            ((NotificationManager) context.getSystemService("notification")).notify(5, build);
//        } catch (Exception unused) {
//        }
//    }

    public static String getTemp(int i, Context context) {
        if (!SharePreferenceUtils.getInstance(context).getTempFormat()) {
            String valueOf = String.valueOf(Math.ceil((double) (((((((float) i) / 10.0f) * 9.0f) / 5.0f) + 32.0f) * 100.0f)) / 100.0d);
            return valueOf + context.getString(R.string.fahrenheit);
        }
        String d = Double.toString(Math.ceil((double) ((((float) i) / 10.0f) * 100.0f)) / 100.0d);
        return d + context.getString(R.string.celsius);
    }

//    public static void showNotificationMemory(Context context) {
//        try {
//            Intent intent = new Intent(context, SpeedBoosterActivity.class);
//            intent.setFlags(268435456);
//            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C0762R.layout.boost_notification);
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.boost_title));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.boost_title2));
//            }
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.boost_des));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.boost_des_2));
//            }
//            remoteViews.setTextViewText(C0762R.C0764id.btnAction, context.getString(C0762R.string.boost_action));
//            NotificationCompat.Builder customContentView = new NotificationCompat.Builder(context).setSmallIcon(C0762R.C0763drawable.ic_noti_small_low_boost).setOngoing(true).setVibrate(new long[]{0}).setOnlyAlertOnce(true).setContentIntent(activity).setAutoCancel(true).setPriority(4).setCustomContentView(remoteViews);
//            if (C0769Utils.isAndroid26()) {
//                createChanelID(context);
//                customContentView.setChannelId("my_channel");
//            }
//            Notification build = customContentView.build();
//            build.flags = 20;
//            ((NotificationManager) context.getSystemService("notification")).notify(2, build);
//        } catch (Exception unused) {
//        }
//    }
//
//    public static void showNotificationLowBattery(Context context) {
//        try {
//            Intent intent = new Intent(context, OptimizeMainActivity.class);
//            intent.setFlags(268435456);
//            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C0762R.layout.low_notification);
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.low_battery));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.low_battery));
//            }
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.low_battery_des));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.low_battery_des));
//            }
//            remoteViews.setTextViewText(C0762R.C0764id.btnAction, context.getString(C0762R.string.optimize));
//            NotificationCompat.Builder customContentView = new NotificationCompat.Builder(context).setSmallIcon(C0762R.C0763drawable.ic_noti_small_low_battery).setOngoing(true).setVibrate(new long[]{0}).setOnlyAlertOnce(true).setContentIntent(activity).setAutoCancel(true).setPriority(4).setCustomContentView(remoteViews);
//            if (C0769Utils.isAndroid26()) {
//                createChanelID(context);
//                customContentView.setChannelId("my_channel");
//            }
//            Notification build = customContentView.build();
//            build.flags = 20;
//            ((NotificationManager) context.getSystemService("notification")).notify(7, build);
//        } catch (Exception unused) {
//        }
//    }
//
//    public static void showNotificationOptimize(Context context) {
//        try {
//            Intent intent = new Intent(context, OptimizeMainActivity.class);
//            intent.setFlags(268435456);
//            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), C0762R.layout.optimize_notification);
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.low_power_30));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitle, context.getString(C0762R.string.low_power_30));
//            }
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.low_battery_des));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.tvNotiTitleDes, context.getString(C0762R.string.low_battery_des));
//            }
//            if (C0769Utils.getRamdom(2) == 0) {
//                remoteViews.setTextViewText(C0762R.C0764id.btnAction, context.getString(C0762R.string.saving_now));
//            } else {
//                remoteViews.setTextViewText(C0762R.C0764id.btnAction, context.getString(C0762R.string.optimize));
//            }
//            NotificationCompat.Builder customContentView = new NotificationCompat.Builder(context).setSmallIcon(C0762R.C0763drawable.ic_noti_small_low_battery).setOngoing(true).setVibrate(new long[]{0}).setOnlyAlertOnce(true).setContentIntent(activity).setAutoCancel(true).setPriority(4).setCustomContentView(remoteViews);
//            if (C0769Utils.isAndroid26()) {
//                createChanelID(context);
//                customContentView.setChannelId("my_channel");
//            }
//            Notification build = customContentView.build();
//            build.flags = 20;
//            ((NotificationManager) context.getSystemService("notification")).notify(4, build);
//        } catch (Exception unused) {
//        }
//    }

    public static void cancle(Context context, int i) {
        ((NotificationManager) context.getSystemService("notification")).cancel(i);
    }

//    public static void showNotificationJunk(Context context) {
//        try {
//            Intent intent = new Intent(context, CleanUpActivity.class);
//            intent.setFlags(268435456);
//            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
//            NotificationCompat.Builder customContentView = new NotificationCompat.Builder(context).setSmallIcon(C0762R.mipmap.ic_launcher).setOngoing(true).setContentIntent(activity).setAutoCancel(true).setCustomContentView(new RemoteViews(context.getPackageName(), C0762R.layout.fragment_clean));
//            if (Utils.Companion.isAndroid26()) {
//                customContentView.setPriority(4);
//                createChanelID(context);
//                customContentView.setChannelId("my_channel");
//            } else {
//                customContentView.setPriority(2);
//            }
//            Notification build = customContentView.build();
//            build.flags = 20;
//            ((NotificationManager) context.getSystemService("notification")).notify(3, build);
//        } catch (Exception unused) {
//        }
//    }

    public static void showNotificationBatteryFull(Context context) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(268435456);
            PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 268435456);
            NotificationCompat.Builder customContentView = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_noti_small_full).setOngoing(true).setVibrate(new long[]{0}).setOnlyAlertOnce(true).setContentIntent(activity).setAutoCancel(true).setPriority(4).setCustomContentView(new RemoteViews(context.getPackageName(), R.layout.notification_battery_full));
            if (Utils.Companion.isAndroid26()) {
                createChanelID(context);
                customContentView.setChannelId("my_channel");
            }
            Notification build = customContentView.build();
            build.flags = 20;
            ((NotificationManager) context.getSystemService("notification")).notify(6, build);
        } catch (Exception unused) {
        }
    }

    @TargetApi(26)
    public static void createChanelID(Context context) {
        try {
            String string = context.getString(R.string.app_name);
            String string2 = context.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel("my_channel", string, 2);
            notificationChannel.setImportance(4);
            notificationChannel.setDescription(string2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            ((NotificationManager) context.getSystemService("notification")).createNotificationChannel(notificationChannel);
        } catch (Exception unused) {
        }
    }
}
