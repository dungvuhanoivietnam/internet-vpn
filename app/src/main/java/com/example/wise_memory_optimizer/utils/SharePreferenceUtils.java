package com.example.wise_memory_optimizer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharePreferenceUtils {
    private static SharePreferenceUtils instance;
    private Context mContext;
    private SharedPreferences pre;
    private SharedPreferences.Editor  editor;

    private SharePreferenceUtils(Context context) {
        this.mContext = context;
        this.pre = context.getSharedPreferences(SharePreferenceConstant.KEY_DATA, 4);
        editor = this.pre.edit();
    }

    public static SharePreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreferenceUtils(context);
        }
        return instance;
    }

    public String getLanguage() {
        return this.pre.getString(SharePreferenceConstant.LANGUAGE, "en");
    }

    public void saveLanguageChange(boolean z) {
        this.editor.putBoolean("LanguageChange", z);
        this.editor.commit();
    }

    public Boolean getLanguageChange() {
        return Boolean.valueOf(this.pre.getBoolean("LanguageChange", false));
    }

    public void saveLanguage(String str) {
        this.editor.putString(SharePreferenceConstant.LANGUAGE, str);
        this.editor.commit();
    }

    public boolean getFirstRun() {
        boolean z = this.pre.getBoolean("first_run_app", true);
        if (z) {
            this.editor.putBoolean("first_run_app", false);
            this.editor.commit();
        }
        return z;
    }

    public long getLevelOut() {
        return this.pre.getLong("LevelOut", 0);
    }

    public void setLevelOut(long j) {
        this.editor.putLong("LevelOut", j);
        this.editor.commit();
    }


    public ArrayList<ApplicationInfo> getAppList() {
        ArrayList<ApplicationInfo> arrayList = new ArrayList<>();
        Type type = new TypeToken<ArrayList<ApplicationInfo>>() {
        }.getType();
        String string = this.pre.getString(SharePreferenceConstant.KEY_APPS_LIST, (String) null);
        return string != null ? (ArrayList) new Gson().fromJson(string, type) : arrayList;
    }

    public long getTimeIn() {
        return this.pre.getLong("TimeIn", 0);
    }

    public void setTimeIn(long j) {
        this.editor.putLong("TimeIn", j);
        this.editor.commit();
    }

    public long getTimeOut() {
        return this.pre.getLong("TimeOut", 0);
    }

    public void setTimeOut(long j) {
        this.editor.putLong("TimeOut", j);
        this.editor.commit();
    }

    public long getTime() {
        return this.pre.getLong("Time", 0);
    }

    public void setTime(long j) {
        this.editor.putLong("Time", j);
        this.editor.commit();
    }

    public boolean getEnable() {
        return this.pre.getBoolean("Enable", false);
    }

    public long getChargeNormal() {
        return this.pre.getLong("ChargeNormal", 0);
    }

    public void setChargeNormal(long j) {
        this.editor.putLong("ChargeNormal", j);
        this.editor.commit();
    }

    public long getChargeHealthy() {
        return this.pre.getLong("ChargeHealthy", 0);
    }

    public void setChargeHealthy(long j) {
        this.editor.putLong("ChargeHealthy", j);
        this.editor.commit();
    }

    public long getChargeOver() {
        return this.pre.getLong("ChargeOver", 0);
    }

    public void setChargeOver(long j) {
        this.editor.putLong("ChargeOver", j);
        this.editor.commit();
    }

    public String getChargeFull() {
        return this.pre.getString("TimeFull", (String) null);
    }

    public void setChargeFull(String str) {
        this.editor.putString("TimeFull", str);
        this.editor.commit();
    }

    public String getChargeType() {
        return this.pre.getString("ChargeType", (String) null);
    }

    public void setChargeType(String str) {
        this.editor.putString("ChargeType", str);
        this.editor.commit();
    }

    public long getLevelIn() {
        return this.pre.getLong("LevelIn", 0);
    }

    public void setLevelIn(long j) {
        this.editor.putLong("LevelIn", j);
        this.editor.commit();
    }

    public long getTimeCharge() {
        return this.pre.getLong("TimeCharge", 0);
    }

    public void setTimeCharge(long j) {
        this.editor.putLong("TimeCharge", j);
        this.editor.commit();
    }

    public long getChargeQuantity() {
        return this.pre.getLong("ChargeQuantity", 0);
    }

    public void setChargeQuantity(long j) {
        this.editor.putLong("ChargeQuantity", j);
        this.editor.commit();
    }

    public boolean getKillApp() {
        return this.pre.getBoolean("KillApp", true);
    }

    public void setKillApp(boolean z) {
        this.editor.putBoolean("KillApp", z);
        this.editor.commit();
    }

    public boolean getWifiStatus() {
        return this.pre.getBoolean("WifiStatus", true);
    }

    public void setWifiStatus(boolean z) {
        this.editor.putBoolean("WifiStatus", z);
        this.editor.commit();
    }

    public boolean getAutoBrightness() {
        return this.pre.getBoolean("AutoBrightness", true);
    }

    public void setAutoBrightness(boolean z) {
        this.editor.putBoolean("AutoBrightness", z);
        this.editor.commit();
    }

    public boolean getBluetoothStatus() {
        return this.pre.getBoolean("BluetoothStatus", true);
    }

    public void setBluetoothStatus(boolean z) {
        this.editor.putBoolean("BluetoothStatus", z);
        this.editor.commit();
    }

    public boolean getAutoSync() {
        return this.pre.getBoolean("AutoSync", true);
    }

    public void setAutoSyncs(boolean z) {
        this.editor.putBoolean("AutoSync", z);
        this.editor.commit();
    }

    public boolean getAutoRunSaverMode() {
        return this.pre.getBoolean("AutoRunSaverMode", false);
    }

    public boolean getFlagSaveMode() {
        return this.pre.getBoolean("FlagSaveMode", false);
    }

    public void setAutoRunSaverMode(boolean z) {
        this.editor.putBoolean("AutoRunSaverMode", z);
        this.editor.commit();
    }

    public boolean getAdd() {
        return this.pre.getBoolean("AddBatteryPlan", true);
    }

    public void setAdd(boolean z) {
        this.editor.putBoolean("AddBatteryPlan", z);
        this.editor.commit();
    }

    public int getPostion() {
        return this.pre.getInt("Postion", 2);
    }

    public void setPostion(int i) {
        this.editor.putInt("Postion", i);
        this.editor.commit();
    }

    public int getTypeMode() {
        return this.pre.getInt("TypeMode", 2);
    }

    public void setTypeMode(int i) {
        this.editor.putInt("TypeMode", i);
        this.editor.commit();
    }

    public boolean getLevelCheck() {
        return this.pre.getBoolean("LevelCheck", true);
    }

    public void setLevelCheck(boolean z) {
        this.editor.putBoolean("LevelCheck", z);
        this.editor.commit();
    }

    public int getTimeOn() {
        return this.pre.getInt("TimeOn", 800);
    }

    public void setTimeOn(int i) {
        this.editor.putInt("TimeOn", i);
        this.editor.commit();
    }

    public int getTimeOff() {
        return this.pre.getInt("TimeOff", 2300);
    }

    public void setTimeOff(int i) {
        this.editor.putInt("TimeOff", i);
        this.editor.commit();
    }

    public boolean getSmartMode() {
        return this.pre.getBoolean("SmartMode", false);
    }

    public void setSmartMode(boolean z) {
        this.editor.putBoolean("SmartMode", z);
        this.editor.commit();
    }

    public boolean getFsWifi() {
        return this.pre.getBoolean("FsWifi", true);
    }

    public void setFsWifi(boolean z) {
        this.editor.putBoolean("FsWifi", z);
        this.editor.commit();
    }

    public boolean getFsBluetooth() {
        return this.pre.getBoolean("FsBluetooth", false);
    }

    public void setFsBluetooth(boolean z) {
        this.editor.putBoolean("FsBluetooth", z);
        this.editor.commit();
    }

    public boolean getFsAutoSync() {
        return this.pre.getBoolean("FsAutoSync", false);
    }

    public void setFsAutoSync(boolean z) {
        this.editor.putBoolean("FsAutoSync", z);
        this.editor.commit();
    }

    public boolean getFsAutoRun() {
        return this.pre.getBoolean("FsAutoRun", false);
    }

    public void setFsAutoRun(boolean z) {
        this.editor.putBoolean("FsAutoRun", z);
        this.editor.commit();
    }

    public boolean getFsAutoBrightness() {
        return this.pre.getBoolean("FsAutoBrightness", true);
    }

    public void setFsAutoBrightness(boolean z) {
        this.editor.putBoolean("FsAutoBrightness", z);
        this.editor.commit();
    }

    public int getBatterySaveModeIndex() {
        return this.pre.getInt("BatterySaveModeIndex", 0);
    }

    public void setBatterySaveModeIndex(int i) {
        this.editor.putInt("BatterySaveModeIndex", i);
        this.editor.commit();
    }

    public int getSaveLevel() {
        return this.pre.getInt("SaveLevel", 30);
    }

    public void setSaveLevel(int i) {
        this.editor.putInt("SaveLevel", i);
        this.editor.commit();
    }

    public int getDndStart() {
        return this.pre.getInt("DndStart", 2200);
    }

    public void setDndStart(int i) {
        this.editor.putInt("DndStart", i);
        this.editor.commit();
    }

    public int getDndStop() {
        return this.pre.getInt("DndStop", 800);
    }

    public void setDndStop(int i) {
        this.editor.putInt("DndStop", i);
        this.editor.commit();
    }

    public boolean getDnd() {
        return this.pre.getBoolean("Dnd", true);
    }

    public void setDnd(boolean z) {
        this.editor.putBoolean("Dnd", z);
        this.editor.commit();
    }

    public boolean getChargeFullReminder() {
        return this.pre.getBoolean("ChargeFullReminder", true);
    }

    public void setChargeFullReminder(boolean z) {
        this.editor.putBoolean("ChargeFullReminder", z);
        this.editor.commit();
    }

    public long getChargeFullReminderTime() {
        return this.pre.getLong("ChargeFullReminderTime", 0);
    }

    public void setChargeFullReminderTime(long j) {
        this.editor.putLong("ChargeFullReminderTime", j);
        this.editor.commit();
    }

    public boolean getLowBatteryReminder() {
        return this.pre.getBoolean("LowBatteryReminder", true);
    }

    public void setLowBatteryReminder(boolean z) {
        this.editor.putBoolean("LowBatteryReminder", z);
        this.editor.commit();
    }

    public boolean getTempFormat() {
        return this.pre.getBoolean("TempFormat", false);
    }

    public void setTempFormat(boolean z) {
        this.editor.putBoolean("TempFormat", z);
        this.editor.commit();
    }

    public boolean getCoolDownReminder() {
        return this.pre.getBoolean("CoolDownReminder", true);
    }

    public void setCoolDownReminder(boolean z) {
        this.editor.putBoolean("CoolDownReminder", z);
        this.editor.commit();
    }

    public boolean getCoolNotification() {
        return this.pre.getBoolean("CoolNotification", false);
    }

    public void setCoolNotification(boolean z) {
        this.editor.putBoolean("CoolNotification", z);
        this.editor.commit();
    }

    public boolean getBoostReminder() {
        return this.pre.getBoolean("BoostReminder", true);
    }

    public void setBoostRemindert(boolean z) {
        this.editor.putBoolean("BoostReminder", z);
        this.editor.commit();
    }

    public long getTotalJunk() {
        return this.pre.getLong("TotalJunk", 0);
    }

    public void setTotalJunk(long j) {
        this.editor.putLong("TotalJunk", j);
        this.editor.commit();
    }

    public long getLowPowerTime() {
        return this.pre.getLong("LowPowerTime", 0);
    }

    public void setLowPowerTime(long j) {
        this.editor.putLong("LowPowerTime", j);
        this.editor.commit();
    }

    public long getOptimizeTime() {
        return this.pre.getLong("OptimizeTime", 0);
    }

    public void setOptimizeTime(long j) {
        this.editor.putLong("OptimizeTime", j);
        this.editor.commit();
    }

    public long getCoolerTime() {
        return this.pre.getLong("CoolerTime", 0);
    }

    public void setCoolerTime(long j) {
        this.editor.putLong("CoolerTime", j);
        this.editor.commit();
    }

    public long getBoostTime() {
        return this.pre.getLong("BoostTime", 0);
    }

    public void setBoostTime(long j) {
        this.editor.putLong("BoostTime", j);
        this.editor.commit();
    }

    public long getCleanTime() {
        return this.pre.getLong("CleanTime", 0);
    }

    public void setCleanTime(long j) {
        this.editor.putLong("CleanTime", j);
        this.editor.commit();
    }

    public boolean getChargeStatus() {
        return this.pre.getBoolean("ChargeStatus", false);
    }

    public void setChargeStatus(boolean z) {
        this.editor.putBoolean("ChargeStatus", z);
        this.editor.commit();
    }

    public long getOptimizeTimeMain() {
        return this.pre.getLong("OptimizeTimeMain", 0);
    }

    public void setOptimizeTimeMain(long j) {
        this.editor.putLong("OptimizeTimeMain", j);
        this.editor.commit();
    }

    public long getCoolerTimeMain() {
        return this.pre.getLong("CoolerTimeMain", 0);
    }

    public void setCoolerTimeMain(long j) {
        this.editor.putLong("CoolerTimeMain", j);
        this.editor.commit();
    }

    public long getBoostTimeMain() {
        return this.pre.getLong("BoostTimeMain", 0);
    }

    public void setBoostTimeMain(long j) {
        this.editor.putLong("BoostTimeMain", j);
        this.editor.commit();
    }

//    public boolean getFlagAds() {
//        return this.pre.getBoolean(AdRequest.LOGTAG, false);
//    }
//
//    public void setFlagAds(boolean z) {
//        this.editor.putBoolean(AdRequest.LOGTAG, z);
//        this.editor.commit();
//    }

    public boolean getNotification() {
        return this.pre.getBoolean("notification_enable", true);
    }

    public void setNotification(boolean z) {
        this.editor.putBoolean("notification_enable", z);
        this.editor.commit();
    }

    public long getHideChargeView() {
        return this.pre.getLong("HideChargeView", 0);
    }

    public void setHideChargeView(long j) {
        this.editor.putLong("HideChargeView", j);
        this.editor.commit();
    }

    public boolean getStatusPer() {
        return this.pre.getBoolean("StatusPer", true);
    }

    public void setStatusPer(boolean z) {
        this.editor.putBoolean("StatusPer", z);
        this.editor.commit();
    }

    public int getLevelScreenOn() {
        return this.pre.getInt("LevelScreenOn", 0);
    }

    public void setLevelScreenOn(int i) {
        this.editor.putInt("LevelScreenOn", i);
        this.editor.commit();
    }

    public boolean getStatusExit() {
        return this.pre.getBoolean("StatusExit", false);
    }

    public void setStatusExit(boolean z) {
        this.editor.putBoolean("StatusExit", z);
        this.editor.commit();
    }

    public void setFlagSaveMode(boolean z) {
        this.editor.putBoolean("FlagSaveMode", z);
        this.editor.commit();
    }
}
