package com.example.wise_memory_optimizer.ui.battery.service;

import com.orhanobut.hawk.Hawk;

public class HawkHelper {
    public static final String BATTERY_LEVEL = "BATTERY_LEVEL";
    public static final String CHARGING_SPEED = "CHARGING_SPEED";
    public static final String CHARGING_SPEED_INDEX = "CHARGING_SPEED_INDEX";
    public static String ENABLE_BLUETOOTH_OFF = "ENABLE_BLUETOOTH_OFF";
    public static String ENABLE_BRIGHTNESS_MIN = "ENABLE_BRIGHTNESS_MIN";
    public static String ENABLE_CLEAR_MEM = "ENABLE_CLEAR_MEM";
    public static String ENABLE_INTERNET_OFF = "ENABLE_INTERNET_OFF";
    public static String ENABLE_ROTATE_OFF = "ENABLE_ROTATE_OFF";
    public static final String IS_CHARGER_CONNECTED = "IS_CHARGER_CONNECTED";
    public static final String IS_FIRST_RUN = "IS_FIRST_RUN";
    public static final String KEY_COUNT_RATE = "KEY_COUNT_RATE";
    public static final String SHOW_RATE = "SHOW_RATE";
    public static final String TRIGGER_ASK_2_RUN = "ASK_2_RUN";
    public static final String TRIGGER_AUTO_RUN = "AUTO_RUN";
    public static final String TRIGGER_EXIT_ON_UNPLUG = "EXIT_ON_DONE";
    public static final String TRIGGER_FULL_BATTERY = "FULL_BATTERY";
    public static final String TRIGGER_NO_RUN = "NO_RUN";
    public static final String TRIGGER_ON_PLUG = "ON_PLUG";
    public static final String TRIGGER_RESTORE_STATE = "RESTORE_STATE";

    public static int getBatteryLevel(int i) {
        return ((Integer) Hawk.get(BATTERY_LEVEL, Integer.valueOf(i))).intValue();
    }

    public static float getChargingSpeed(float f) {
        return ((Float) Hawk.get(CHARGING_SPEED, Float.valueOf(f))).floatValue();
    }

    public static int getChargingSpeedIndex(int i) {
        return ((Integer) Hawk.get(CHARGING_SPEED_INDEX, Integer.valueOf(i))).intValue();
    }

    public static int getKeyCountRate() {
        return ((Integer) Hawk.get(KEY_COUNT_RATE, 0)).intValue();
    }

    public static String getModePlug(String str) {
        return (String) Hawk.get(TRIGGER_ON_PLUG, str);
    }

    public static boolean isBlueTooth() {
        return ((Boolean) Hawk.get(ENABLE_BLUETOOTH_OFF, true)).booleanValue();
    }

    public static boolean isBrightness() {
        return ((Boolean) Hawk.get(ENABLE_BRIGHTNESS_MIN, true)).booleanValue();
    }

    public static boolean isChargerConnected() {
        return ((Boolean) Hawk.get(IS_CHARGER_CONNECTED, false)).booleanValue();
    }

    public static boolean isClearMem() {
        return ((Boolean) Hawk.get(ENABLE_CLEAR_MEM, true)).booleanValue();
    }

    public static boolean isExitUnplug() {
        return ((Boolean) Hawk.get(TRIGGER_EXIT_ON_UNPLUG, true)).booleanValue();
    }

    public static boolean isInternet() {
        return ((Boolean) Hawk.get(ENABLE_INTERNET_OFF, false)).booleanValue();
    }

    public static boolean isRestoreUnplug() {
        return ((Boolean) Hawk.get(TRIGGER_RESTORE_STATE, true)).booleanValue();
    }

    public static boolean isRotate() {
        return ((Boolean) Hawk.get(ENABLE_ROTATE_OFF, true)).booleanValue();
    }

    public static boolean isShowRate() {
        return ((Boolean) Hawk.get(SHOW_RATE, false)).booleanValue();
    }

    public static boolean isTriggerFullBattery() {
        return ((Boolean) Hawk.get(TRIGGER_FULL_BATTERY, true)).booleanValue();
    }

    public static void setBatteryLevel(int i) {
        Hawk.put(BATTERY_LEVEL, Integer.valueOf(i));
    }

    public static void setBlueTooth(boolean z) {
        Hawk.put(ENABLE_BLUETOOTH_OFF, Boolean.valueOf(z));
    }

    public static void setBrightness(boolean z) {
        Hawk.put(ENABLE_BRIGHTNESS_MIN, Boolean.valueOf(z));
    }

    public static void setChargerConnected(boolean isConnected) {
        Hawk.put(IS_CHARGER_CONNECTED, isConnected);
    }

    public static void setChargingSpeed(float f) {
        Hawk.put(CHARGING_SPEED, Float.valueOf(f));
    }

    public static void setChargingSpeedIndex(int i) {
        Hawk.put(CHARGING_SPEED_INDEX, Integer.valueOf(i));
    }

    public static void setClearMem(boolean z) {
        Hawk.put(ENABLE_CLEAR_MEM, Boolean.valueOf(z));
    }

    public static void setExitUnplug(boolean z) {
        Hawk.put(TRIGGER_EXIT_ON_UNPLUG, Boolean.valueOf(z));
    }

    public static void setInternet(boolean z) {
        Hawk.put(ENABLE_INTERNET_OFF, Boolean.valueOf(z));
    }

    public static void setKeyCountRate(int i) {
        Hawk.put(KEY_COUNT_RATE, Integer.valueOf(i));
    }

    public static void setModePlug(String str) {
        Hawk.put(TRIGGER_ON_PLUG, str);
    }

    public static void setRestoreUnplug(boolean z) {
        Hawk.put(TRIGGER_RESTORE_STATE, Boolean.valueOf(z));
    }

    public static void setRotate(boolean z) {
        Hawk.put(ENABLE_ROTATE_OFF, Boolean.valueOf(z));
    }

    public static void setShowRate(boolean z) {
        Hawk.put(SHOW_RATE, Boolean.valueOf(z));
    }

    public static void setTriggerFullBattery(boolean z) {
        Hawk.put(TRIGGER_FULL_BATTERY, Boolean.valueOf(z));
    }
}
