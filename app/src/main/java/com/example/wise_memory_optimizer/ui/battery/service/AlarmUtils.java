package com.example.wise_memory_optimizer.ui.battery.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;

public class AlarmUtils {
    public static final String ACTION_AUTOSTART_ALARM = "com.app.action.alarmmanager";
    public static final String ACTION_CHECK_BATTERY_FULL = "action_check_battery_full";
    public static final String ACTION_CHECK_DEVICE_STATUS = "action_check_devic_status";
    public static final String ACTION_REPEAT_SERVICE = "action_repeat_service";
    public static final String ACTION_SMART_OFF = "action_smart_off";
    public static final String ACTION_SMART_ON = "action_smart_on";
    public static final int CHECK_BATTERY_FULL_CODE = 1006;
    public static final int CHECK_DEVICE_STATUS_CODE = 1002;
    public static final int DEFAULT_CODE = 1000;
    public static final int REPREAT_SERVICE_CODE = 1001;
    public static final int SMART_OFF_CODE = 1004;
    public static final int SMART_ON_CODE = 1003;
    public static final int TIME_CHECK_BATTERY_FULL = 300000;
    public static final int TIME_CHECK_DEVICE_STATUS = 300000;
    public static final int TIME_REPREAT_SERVICE = 1000;
    public static final int TIME_SHOULD_DOING_BOOSTER = 36000000;
    public static final int TIME_SHOULD_DOING_BOOSTER_MAIN = 180000;
    public static final int TIME_SHOULD_DOING_CLEAN = 300000;
    public static final int TIME_SHOULD_DOING_COOLER = 36000000;
    public static final int TIME_SHOULD_DOING_COOLER_MAIN = 180000;
    public static final int TIME_SHOULD_DOING_OPTIMIZE = 21600000;
    public static final int TIME_SHOULD_DOING_OPTIMIZE_MAIN = 180000;
    public static final int TIME_SHOULD_DOING_SHOW_DIALOG_FAST_CHARGE = 3600000;
    public static final int TIME_SHOULD_FAST_CHARGE = 300000;

    public static void cancel(Context context, String str) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_AUTOSTART_ALARM);
        ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(PendingIntent.getBroadcast(context, getRequestCode(str), intent, 134217728));
    }

    public static Calendar getCalendar(int i) {
        Calendar instance = Calendar.getInstance();
        instance.set(11, i / 100);
        instance.set(12, i % 100);
        instance.set(13, 0);
        if (instance.getTimeInMillis() < System.currentTimeMillis()) {
            instance.add(5, 1);
        }
        return instance;
    }

    public static void setAlarm(Context context, String str, int i) {
        Calendar calendar = getCalendar(i);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_AUTOSTART_ALARM);
        intent.putExtra(str, Boolean.TRUE);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, getRequestCode(str), intent, 134217728);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        alarmManager.cancel(broadcast);
        long currentTimeMillis = System.currentTimeMillis() + ((long) i);
        if (str.equals(ACTION_SMART_ON) || str.equals(ACTION_SMART_OFF)) {
            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, 0, calendar.getTimeInMillis(), broadcast);
        } else {
            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, 0, currentTimeMillis, broadcast);
        }
    }

    public static int getRequestCode(String str) {
        if (str.equals(ACTION_REPEAT_SERVICE)) {
            return 1001;
        }
        if (str.equals(ACTION_CHECK_DEVICE_STATUS)) {
            return 1002;
        }
        if (str.equals(ACTION_SMART_ON)) {
            return 1003;
        }
        if (str.equals(ACTION_SMART_OFF)) {
            return 1004;
        }
        return str.equals(ACTION_CHECK_BATTERY_FULL) ? 1006 : 1000;
    }
}
