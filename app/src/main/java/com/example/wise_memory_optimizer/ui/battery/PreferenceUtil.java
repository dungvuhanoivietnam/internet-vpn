package com.example.wise_memory_optimizer.ui.battery;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferenceUtil {

    // save view auto optimize
    public static final String SETTING_AUTO_OPTIMIZE = "SETTING_OPTIMIZE";
    public static final String SETTING_RAM = "SETTING_RAM";
    public static final String PROGRESS_RAM = "PROGRESS_RAM";
    public static final String PROGRESS_CPU = "PROGRESS_CPU";
    public static final String SETTING_CPU = "SETTING_CPU";
    public static final String SETTING_PIN = "SETTING_PIN";
    public static final String PROGRESS_PIN = "PROGRESS_PIN";
    // end

    // save english
    public static final String SETTING_ENGLISH = "SETTING_ENGLISH";
    // end



    public static final String AUTO_BOOST = "auto_boost";
    public static final String BATTERY_PERCENT = "battery_percent";
    public static final String BATTERY_PLAN = "battery_plan";
    public static final String BATTERY_SAVER = "battery_saver";
    public static final String FLOATING_BOOSTER = "floating_booster";
    public static final String FULLY_BATTERY = "fully_battery";
    private static final String MyPREFERENCES = "MyPreferences";
    public static final String PEDIOD = "pediod";
    public static final String PEDIOD_INDEX = "pediod_index";
    public static final String PEDIOD_OUTSIDE = "pediod_outside";
    public static final String PEDIOD_OUTSIDE_INDEX = "pediod_outside_index";
    public static final String TIME_START_HOUR = "time_start_hour";
    public static final String TIME_START_MINUTE = "time_start_minute";
    public static final String TIME_STOP_HOUR = "time_stop_hour";
    public static final String TIME_STOP_MINUTE = "time_stop_minutes";
    private static final String WHITE_LIST = "whitelist";

    public void saveBatterySaver(Context context, List<BatteryInfo> list) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putString(BATTERY_SAVER, new Gson().toJson((Object) list));
        edit.apply();
    }

    public void removeAll(Context context) {
        context.getSharedPreferences(MyPREFERENCES, 0).edit().remove(BATTERY_SAVER).apply();
    }

    public List<BatteryInfo> getListBatterySaver(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, 0);
        if (!sharedPreferences.contains(BATTERY_SAVER)) {
            return null;
        }
        return new ArrayList(Arrays.asList((BatteryInfo[]) new Gson().fromJson(sharedPreferences.getString(BATTERY_SAVER, (String) null), BatteryInfo[].class)));
    }

    public static void saveBoolean(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return context.getSharedPreferences(MyPREFERENCES, 0).getBoolean(str, z);
    }

    public static void saveInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getInt(Context context, String str) {
        return context.getSharedPreferences(MyPREFERENCES, 0).getInt(str, 0);
    }

    public static void saveString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(MyPREFERENCES, 0).getString(str, str2);
    }

    public void saveLocked(Context context, List<String> list) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putString(WHITE_LIST, new Gson().toJson((Object) list));
        edit.apply();
    }

    public void addLocked(Context context, String str) {
        ArrayList<String> locked = getLocked(context);
        if (locked == null) {
            locked = new ArrayList<>();
        }
        locked.add(str);
        saveLocked(context, locked);
    }

    public void removeLocked(Context context, String str) {
        ArrayList<String> locked = getLocked(context);
        if (locked != null) {
            locked.remove(str);
            saveLocked(context, locked);
        }
    }

    public ArrayList<String> getLocked(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, 0);
        if (!sharedPreferences.contains(WHITE_LIST)) {
            return null;
        }
        return new ArrayList<>(Arrays.asList((String[]) new Gson().fromJson(sharedPreferences.getString(WHITE_LIST, (String) null), String[].class)));
    }
}
