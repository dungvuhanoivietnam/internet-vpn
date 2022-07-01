package com.example.wise_memory_optimizer.ui.battery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.Calendar;

public class HistoryPref {
    public static final int DEFAULT_LEVEL = -1;
    private static final String HISTORY_PREF = ("history_info" + "history_info".hashCode());
    public static final int NUMBER_POINT_IN_PER_4_HOUR = 4;

    public static void putLevel(Context context, int i) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        int i2 = instance.get(12);
        if (i2 <= 3 || i2 >= 57) {
            if (i2 >= 57) {
                instance.add(11, 1);
            }
            int i3 = instance.get(5);
            int i4 = instance.get(11);
            putTimeNow(context, i3, i4, i);
            if (!context.getSharedPreferences(HISTORY_PREF, 0).contains(getKeyFromTime(i3, i4))) {
                putLevel(context, i3, i4, i);
                return;
            }
            return;
        }
        instance.add(11, 1);
        putTimeNow(context, instance.get(5), instance.get(11), i);
        context.getSharedPreferences(HISTORY_PREF, 0);
        instance2.add(11, -2);
        removeLevel(context, getKeyFromTimeNow(instance2.get(5), instance2.get(11)));
    }

    public static void putTimeNow(Context context, int i, int i2, int i3) {
        SharedPreferences.Editor edit = context.getSharedPreferences(HISTORY_PREF, 0).edit();
        edit.putInt("bat_time_now_" + String.valueOf(i) + "_" + String.valueOf(i2), i3);
        Log.e("X", "putTimeNow bat_time_" + String.valueOf(i) + "_" + String.valueOf(i2) + " " + i3 + " %");
        edit.apply();
    }

    public static void putLevel(Context context, int i, int i2, int i3) {
        SharedPreferences.Editor edit = context.getSharedPreferences(HISTORY_PREF, 0).edit();
        edit.putInt("bat_time_" + String.valueOf(i) + "_" + String.valueOf(i2), i3);
        Log.e("HHHHH", "Put vao bat_time_" + String.valueOf(i) + "_" + String.valueOf(i2) + " " + i3 + " %");
        edit.apply();
    }

    public static int getLevel(Context context, String str) {
        return context.getSharedPreferences(HISTORY_PREF, 0).getInt(str, -1);
    }

    public static void removeLevel(Context context, String str) {
        context.getSharedPreferences(HISTORY_PREF, 0).edit().remove(str).apply();
    }

    public static String getKeyFromTime(int i, int i2) {
        Log.e("HHHHH", "getKeyFromTime bat_time_" + String.valueOf(i) + "_" + String.valueOf(i2));
        return "bat_time_" + String.valueOf(i) + "_" + String.valueOf(i2);
    }

    public static String getKeyFromTimeNow(int i, int i2) {
        Log.e("HHHHH", "bat_time_now_" + String.valueOf(i) + "_" + String.valueOf(i2));
        return "bat_time_now_" + String.valueOf(i) + "_" + String.valueOf(i2);
    }
}
