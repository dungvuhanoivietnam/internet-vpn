package com.example.wise_memory_optimizer.ui.battery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import com.github.mikephil.charting.utils.Utils;


public class BatteryPref {
    public static final String BATTERY_PREF = ("battery_info" + "battery_info".hashCode());
    public static final String EXTRA_CURENT_TIME1 = "curenttime1";
    public static final String EXTRA_CURENT_TIME2 = "curenttime2";
    public static final String EXTRA_CURENT_TIME3 = "curenttime3";
    public static final String EXTRA_CURENT_TIME4 = "curenttime4";
    public static final String EXTRA_CURENT_TIME5 = "curenttime5";
    public static final String EXTRA_CURENT_TIME_CHARGE_AC = "time_charge_ac";
    public static final String EXTRA_CURENT_TIME_CHARGE_USB = "time_charge_USB";
    public static final String EXTRA_LEVEL = "level";
    public static final String EXTRA_TIME_CHARGING_AC = "timecharging_ac";
    public static final String EXTRA_TIME_CHARGING_USB = "timecharging_usb";
    public static final String EXTRA_TIME_REMAIN = "timeremainning";
    public static final String TIMEMAIN1 = "timemain1";
    public static final String TIMEMAIN2 = "timemain2";
    public static final String TIMEMAIN3 = "timemain3";
    public static final String TIMEMAIN4 = "timemain4";
    public static final String TIMEMAIN5 = "timemain5";
    public static final String TIMESCREENOFF = "time_Screen_on";
    public static final String TIMESCREENON = "time_Screen_on";
    public static final long TIME_CHARGING_AC_DEFAULT = 216000;
    public static final long TIME_CHARGING_AC_MAX = 108000;
    public static final long TIME_CHARGING_AC_MIN = 36000;
    public static final long TIME_CHARGING_USB_DEFAULT = 144000;
    public static final long TIME_CHARGING_USB_MAX = 180000;
    public static final long TIME_CHARGING_USB_MIN = 108000;
    public static long TIME_REMAIN_DEFAULT = 864000;
    public static final long TIME_REMAIN_MAX = 2160000;
    public static final long TIME_REMAIN_MIN = 720000;
    private static BatteryPref batteryPref;
    static Context mContext;

    private BatteryPref(Context context) {
        Log.e("TIME_REMAIN_DEFAULT", String.valueOf(TIME_REMAIN_DEFAULT));
        long round = (Math.round(getBatteryCapacity(context)) * 24) / 3;
        TIME_REMAIN_DEFAULT = (TIME_REMAIN_DEFAULT * Math.round(getBatteryCapacity(context))) / 3200;
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT).commit();
        if (!sharedPreferences.contains(EXTRA_TIME_REMAIN) || !sharedPreferences.contains(EXTRA_CURENT_TIME1) || !sharedPreferences.contains(EXTRA_TIME_CHARGING_AC) || !sharedPreferences.contains(EXTRA_TIME_CHARGING_USB)) {
            sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_AC, TIME_CHARGING_AC_DEFAULT).commit();
            sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_USB, TIME_CHARGING_USB_DEFAULT).commit();
        }
    }

    public static double getBatteryCapacity(Context context) {
        Object obj;
        try {
            obj = Class.forName("com.android.internal.os.PowerProfile").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        try {
            return ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getAveragePower", new Class[]{String.class}).invoke(obj, new Object[]{"battery.capacity"})).doubleValue();
        } catch (Exception e2) {
            e2.printStackTrace();
            return Utils.DOUBLE_EPSILON;
        }
    }

    public static BatteryPref initilaze(Context context) {
        mContext = context;
        if (batteryPref == null) {
            batteryPref = new BatteryPref(mContext);
        }
        return batteryPref;
    }

    public void putLevel(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        if (i != getLevel(context)) {
            sharedPreferences.edit().putInt("level", i).commit();
        }
    }

    public void setScreenOn() {
        mContext.getSharedPreferences(BATTERY_PREF, 0).edit().putLong("time_Screen_on", System.currentTimeMillis()).commit();
    }

    public void setScreenOff() {
        mContext.getSharedPreferences(BATTERY_PREF, 0).edit().putLong("time_Screen_on", System.currentTimeMillis()).commit();
    }

    public int getTimeRemainning(Context context, int i) {
        Context context2 = context;
        int i2 = i;
        SharedPreferences sharedPreferences = context2.getSharedPreferences(BATTERY_PREF, 0);
        ((PowerManager) context2.getSystemService("power")).isScreenOn();
        int i3 = (int) ((((long) i2) * sharedPreferences.getLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT)) / 60000);
        if (getLevel(context) == -1) {
            putLevel(context, i);
        } else if (i2 < getLevel(context)) {
            sharedPreferences.edit().putInt("level", i2).commit();
            if (sharedPreferences.getLong(EXTRA_CURENT_TIME1, 0) != 0 && sharedPreferences.getLong(EXTRA_CURENT_TIME2, 0) != 0 && sharedPreferences.getLong(EXTRA_CURENT_TIME3, 0) != 0) {
                long j = sharedPreferences.getLong(TIMEMAIN1, 0);
                long j2 = sharedPreferences.getLong(TIMEMAIN2, 0);
                long j3 = sharedPreferences.getLong(TIMEMAIN3, 0);
                long j4 = sharedPreferences.getLong(TIMEMAIN4, 0);
                long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME5, System.currentTimeMillis());
                long j5 = currentTimeMillis;
                long j6 = ((((((j + j2) + j3) + j4) + currentTimeMillis) + TIME_REMAIN_DEFAULT) + sharedPreferences.getLong(EXTRA_TIME_REMAIN, TIME_REMAIN_DEFAULT)) / 7;
                if (j6 > TIME_REMAIN_MIN) {
                    if (j6 < TIME_REMAIN_MAX) {
                        sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, j6).commit();
                    } else {
                        sharedPreferences.edit().putLong(EXTRA_TIME_REMAIN, 1080000).commit();
                    }
                }
                Log.e("ALO ", "timeRemain1 " + String.valueOf(j / 1000) + " giay");
                Log.e("ALO ", "timeRemain2 " + String.valueOf(j2 / 1000) + " giay");
                Log.e("ALO ", "timeRemain3 " + String.valueOf(j3 / 1000) + " giay");
                Log.e("ALO ", "timeRemain4 " + String.valueOf(j4 / 1000) + " giay");
                Log.e("ALO ", "timeRemain5 " + String.valueOf(j5 / 1000) + " giay");
                Log.e("ALO ", "timeRemain main  " + String.valueOf(j6 / 1000) + " giay");
                sharedPreferences.edit().putLong(TIMEMAIN1, j).commit();
                sharedPreferences.edit().putLong(TIMEMAIN2, j2).commit();
                sharedPreferences.edit().putLong(TIMEMAIN3, j3).commit();
                sharedPreferences.edit().putLong(TIMEMAIN4, j4).commit();
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME3, System.currentTimeMillis()).commit();
            } else if (sharedPreferences.getLong(EXTRA_CURENT_TIME1, 0) == 0) {
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME1, System.currentTimeMillis()).commit();
                return i3;
            } else if (sharedPreferences.getLong(EXTRA_CURENT_TIME2, 0) == 0) {
                sharedPreferences.edit().putLong(TIMEMAIN1, System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())).commit();
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME2, System.currentTimeMillis()).commit();
                Log.e("ALO ", "timeRemain1111 " + String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())) / 1000) + " giay");
                return i3;
            } else if (sharedPreferences.getLong(EXTRA_CURENT_TIME3, 0) == 0) {
                sharedPreferences.edit().putLong(TIMEMAIN2, System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())).commit();
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME3, System.currentTimeMillis()).commit();
                Log.e("ALO ", "timeRemain1111 " + String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())) / 1000) + " giay");
                return i3;
            } else if (sharedPreferences.getLong(EXTRA_CURENT_TIME4, 0) == 0) {
                sharedPreferences.edit().putLong(TIMEMAIN3, System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())).commit();
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME4, System.currentTimeMillis()).commit();
                Log.e("ALO ", "timeRemain1111 " + String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())) / 1000) + " giay");
                return i3;
            } else if (sharedPreferences.getLong(EXTRA_CURENT_TIME5, 0) != 0) {
                return i3;
            } else {
                sharedPreferences.edit().putLong(TIMEMAIN4, System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())).commit();
                sharedPreferences.edit().putLong(EXTRA_CURENT_TIME5, System.currentTimeMillis()).commit();
                Log.e("ALO ", "timeRemain1111 " + String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME1, System.currentTimeMillis())) / 1000) + " giay");
                return i3;
            }
        }
        return i3;
    }

    public int getTimeChargingUsb(Context context, int i) {
        Log.e("TIME_REMAIN_DEFAULT getBatteryCapacity(context))", String.valueOf(TIME_REMAIN_DEFAULT));
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        int i2 = (int) ((((long) (100 - i)) * sharedPreferences.getLong(EXTRA_TIME_CHARGING_USB, TIME_CHARGING_USB_DEFAULT)) / 60000);
        if (i > getLevel(context)) {
            sharedPreferences.edit().putInt("level", i).commit();
            long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME_CHARGE_USB, System.currentTimeMillis());
            if (currentTimeMillis < TIME_CHARGING_USB_MAX && currentTimeMillis > 108000) {
                sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_USB, currentTimeMillis).commit();
            }
        }
        return i2;
    }

    public int getTimeChargingAc(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        int i2 = (int) ((((long) (100 - i)) * sharedPreferences.getLong(EXTRA_TIME_CHARGING_AC, TIME_CHARGING_AC_DEFAULT)) / 60000);
        if (i > getLevel(context)) {
            sharedPreferences.edit().putInt("level", i).commit();
            long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME_CHARGE_AC, System.currentTimeMillis());
            if (currentTimeMillis < 108000 && currentTimeMillis > TIME_CHARGING_AC_MIN) {
                sharedPreferences.edit().putLong(EXTRA_TIME_CHARGING_AC, currentTimeMillis).commit();
            }
        }
        return i2;
    }

    public int getLevel(Context context) {
        return context.getSharedPreferences(BATTERY_PREF, 0).getInt("level", -1);
    }
}
