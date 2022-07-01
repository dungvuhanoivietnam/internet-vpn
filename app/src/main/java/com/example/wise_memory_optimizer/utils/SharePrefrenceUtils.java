package com.example.wise_memory_optimizer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefrenceUtils {

    public static final String KEY_VPN = "keyvpn";
    public static final String KEY_VPN_SAVE = "keyvpnsave";
    public static final String KEY_VPN_SAVE_IP = KEY_VPN_SAVE + "ip";
    public static final String KEY_VPN_SAVE_DOWN = KEY_VPN_SAVE + "down";
    public static final String KEY_VPN_SAVE_UP = KEY_VPN_SAVE + "up";
    public static final String KEY_VPN_DF_CITY = KEY_VPN_SAVE + "dfCity";

    private SharedPreferences mPreference;
    private SharedPreferences.Editor mPrefEditor;

    public static SharePrefrenceUtils getInstance(Context context) {
        return new SharePrefrenceUtils(context);
    }

    public SharePrefrenceUtils(Context context) {
        mPreference = context.getSharedPreferences(KEY_VPN, Context.MODE_PRIVATE);
        mPrefEditor = mPreference.edit();
    }

    public boolean saveDfCity(String code) {
        mPrefEditor.putString(KEY_VPN_DF_CITY, code);
        return mPrefEditor.commit();
    }

    public String getDfCity() {
        return mPreference.getString(KEY_VPN_DF_CITY, "");
    }

    public boolean saveDown(String code, String ip) {
        mPrefEditor.putString(KEY_VPN_SAVE_DOWN + code, ip);
        return mPrefEditor.commit();
    }

    public String getDown(String code) {
        return mPreference.getString(KEY_VPN_SAVE_DOWN + code, "");
    }

    public boolean saveUp(String code, String speed) {
        mPrefEditor.putString(KEY_VPN_SAVE_UP + code, speed);
        return mPrefEditor.commit();
    }

    public String getup(String code) {
        return mPreference.getString(KEY_VPN_SAVE_UP + code, "");
    }
}
