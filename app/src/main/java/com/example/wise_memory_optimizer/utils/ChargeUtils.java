package com.example.wise_memory_optimizer.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.wise_memory_optimizer.R;
import com.example.wise_memory_optimizer.ui.battery.BatteryPref;
import com.example.wise_memory_optimizer.ui.battery.service.HawkHelper;
import com.example.wise_memory_optimizer.ui.battery.service.LocationScannerImpl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Hoang NS on 1/19/2021.
 */
public class ChargeUtils {

    public static int CUR_CAPACITY = 0;
    public static int BATTERY_LEVEL = 0;
    public static int BATTERY_PLUGGED = 0;
    public static boolean BATTERY_STATUS_FULL = false;

    public static UsageStatsManager usageStatsManager;
    public static final String PARAM_WIFI = "wifi";
    public static final String COMMAND_L_OFF = "svc data disable\n ";
    public static final String COMMAND_L_ON = "svc data enable\n ";
    public static final String COMMAND_SU = "su";
    public static final String SETTINGS_PREFERENCE = "SETTINGS";
    public static final String STATE_BLUETOOTH = "STATE_BLUETOOTH";
    public static final String STATE_BRIGHTNESS = "STATE_REDUCE_BRIGHTNESS";
    public static final String STATE_BRIGHTNESS_MODE = "STATE_BRIGHTNESS_MODE";
    public static final String STATE_MOBILE_DATA = "STATE_MOBILE_DATA";
    public static final String STATE_ROTATE = "STATE_ROTATE";
    public static final String STATE_WIFI = "STATE_WIFI";
    public static final String TRIGGER_ASK_2_RUN = "ASK_2_RUN";
    public static final String TRIGGER_AUTO_RUN = "AUTO_RUN";
    public static final String TRIGGER_NO_RUN = "NO_RUN";


    public static void clearMem(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (int i = 0; i < runningAppProcesses.size(); i++) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcesses.get(i);
                String[] strArr = runningAppProcessInfo.pkgList;
                if (!runningAppProcessInfo.processName.startsWith("com.sec") && (runningAppProcessInfo.importance > 150 || runningAppProcessInfo.processName.contains("google"))) {
                    for (String killBackgroundProcesses : strArr) {
                        activityManager.killBackgroundProcesses(killBackgroundProcesses);
                        Toast.makeText(context, context.getResources().getString(R.string.clear_ram), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    public static boolean checkShouldDoing(Context context, int i) {
        switch (i) {
            case 0:
                if (SharePreferenceUtils.getInstance(context).getOptimizeTime() + 21600000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 1:
                if (SharePreferenceUtils.getInstance(context).getBoostTime() + 43200000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 2:
                if (SharePreferenceUtils.getInstance(context).getCoolerTime() + 43200000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 3:
                if (SharePreferenceUtils.getInstance(context).getCleanTime() + 1800000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 4:
                if (SharePreferenceUtils.getInstance(context).getCleanTime() + 300000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 5:
                if (SharePreferenceUtils.getInstance(context).getOptimizeTimeMain() + 300000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 6:
                if (SharePreferenceUtils.getInstance(context).getBoostTimeMain() + 300000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 7:
                if (SharePreferenceUtils.getInstance(context).getCoolerTimeMain() + 300000 < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 8:
                if (SharePreferenceUtils.getInstance(context).getHideChargeView() + BatteryPref.TIME_CHARGING_USB_MAX < System.currentTimeMillis()) {
                    return true;
                }
                return false;
            case 9:
                return SharePreferenceUtils.getInstance(context).getLowPowerTime() + 600000 < System.currentTimeMillis();
            default:
                return false;
        }
    }

    public static void doOptimize(Context context) {
        if (HawkHelper.isClearMem()) {
            clearMem(context);
        }
        if (HawkHelper.isBrightness()) {
            reduceBrightness(context);
        }
        if (HawkHelper.isBlueTooth()) {
            offBluetooth(context);
        }
        if (HawkHelper.isRotate()) {
            offRotate(context);
        }
    }

    public static void offInternet(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(PARAM_WIFI);
        if (wifiManager.isWifiEnabled()) {
            saveState(STATE_WIFI, wifiManager.isWifiEnabled(), context);
            wifiManager.setWifiEnabled(false);
        }
        saveConnection(context);
        setConnection(false, context);
    }


    public static void offBluetooth(Context context) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && defaultAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            defaultAdapter.disable();
            Toast.makeText(context, context.getResources().getString(R.string.off_bluetooth), Toast.LENGTH_SHORT).show();
        }
    }

    public static void offRotate(Context context) {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(context.getContentResolver(), "accelerometer_rotation", 0);
            Toast.makeText(context, context.getResources().getString(R.string.off_rotate), Toast.LENGTH_SHORT).show();
        }
    }

    public static void reduceBrightness(Context context) {
        if (Settings.System.canWrite(context)) {
            try {
                saveState(STATE_BRIGHTNESS_MODE, Settings.System.getInt(context.getContentResolver(), "screen_brightness_mode"), context);
                float f = Settings.System.getInt(context.getContentResolver(), "screen_brightness");
                saveStateBrightness(STATE_BRIGHTNESS, f, context);
                Settings.System.putInt(context.getContentResolver(), "screen_brightness_mode", 0);
                Settings.System.putInt(context.getContentResolver(), "screen_brightness", (int) ((f * 3.0f) / 10.0f));
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                Log.e("brightness", e.toString());
            }
        }
    }

    public static void saveConnection(Context context) {
        boolean z = true;
        if (Settings.Global.getInt(context.getContentResolver(), "mobile_data", 1) != 1) {
            z = false;
        }
        saveState(STATE_MOBILE_DATA, z, context);
    }

    public static void saveState(String str, int i, Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SETTINGS_PREFERENCE, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static void saveState(String str, boolean z, Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SETTINGS_PREFERENCE, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static void saveStateBrightness(String str, float f, Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SETTINGS_PREFERENCE, 0).edit();
        edit.putFloat(str, f);
        edit.apply();
    }

    public static void setConnection(boolean z, Context context) {
        String str = z ? COMMAND_L_ON : COMMAND_L_OFF;
        try {
            Process exec = Runtime.getRuntime().exec(COMMAND_SU);
            DataOutputStream dataOutputStream = new DataOutputStream(exec.getOutputStream());
            dataOutputStream.writeBytes(str);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            try {
                exec.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataOutputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isWifiEnabled(Context context) {
        return ((WifiManager) context.getApplicationContext().getSystemService(PARAM_WIFI)).isWifiEnabled();
    }

    public static boolean isDeviceRooted() {
        return false;
    }

    public static boolean isRotateOff(Context context) {
        if (Settings.System.canWrite(context)) {
            try {
                return Settings.System.getInt(context.getContentResolver(), "accelerometer_rotation") == 0;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getTopTask(ActivityManager activityManager, UsageStatsManager usageStatsManager2) {
        int i = Build.VERSION.SDK_INT;
        if (i < 21) {
            List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
            return (runningTasks == null || runningTasks.isEmpty()) ? "" : runningTasks.get(0).topActivity.getPackageName();
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (i == 21) {
            return (runningAppProcesses == null || runningAppProcesses.size() == 0) ? "" : runningAppProcesses.get(0).processName;
        }
        if (runningAppProcesses == null || runningAppProcesses.size() == 0) {
            return "";
        }
        String str = runningAppProcesses.get(0).processName;
        long currentTimeMillis = System.currentTimeMillis();
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents queryEvents = usageStatsManager2.queryEvents(currentTimeMillis - 10000, currentTimeMillis);
        String str2 = str;
        while (queryEvents.hasNextEvent()) {
            queryEvents.getNextEvent(event);
            if (event.getEventType() == 1) {
                str2 = event.getPackageName();
            }
        }
        return str2;
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (Build.VERSION.SDK_INT >= 22) {
            usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
        }
        List<ActivityManager.RunningAppProcessInfo> list = null;
        if (Build.VERSION.SDK_INT <= 21) {
            return getTopTask(activityManager, usageStatsManager).equals(context.getPackageName());
        }
        if (activityManager != null) {
            list = activityManager.getRunningAppProcesses();
        }
        return list != null && list.get(0).importance == 100;
    }

    public static boolean isBrightnessReduce(Context context) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(context)) {
            return true;
        }
        try {
            int i = Settings.System.getInt(context.getContentResolver(), "screen_brightness_mode");
            int i2 = Settings.System.getInt(context.getContentResolver(), "screen_brightness");
            if (i2 == getMinimumScreenBrightnessSetting()) {
                return true;
            }
            return i == 0 && i2 == ((int) ((getOldBringtnessSave(STATE_BRIGHTNESS, LocationScannerImpl.MIN_DISTANCE_BETWEEN_UPDATES, context) * 3.0f) / 10.0f));
        } catch (Settings.SettingNotFoundException unused) {
            return true;
        }
    }


    public static float getOldBringtnessSave(String str, float f, Context context) {
        return context.getSharedPreferences(SETTINGS_PREFERENCE, 0).getFloat(str, f);
    }

    public static int getMinimumScreenBrightnessSetting() {
        Resources system = Resources.getSystem();
        int identifier = system.getIdentifier("config_screenBrightnessSettingMinimum", "integer", "android");
        if (identifier == 0) {
            identifier = system.getIdentifier("config_screenBrightnessDim", "integer", "android");
        }
        if (identifier == 0) {
            return 0;
        }
        try {
            return system.getInteger(identifier);
        } catch (Resources.NotFoundException unused) {
            return 0;
        }
    }

    public static boolean isBluetoothEnabled(Context context) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            return defaultAdapter.isEnabled();
        }
        return false;
    }
}
