package com.example.wise_memory_optimizer.ui.battery.service

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.ui.battery.service.BatteryService.ACTION_BATTERY_CHANGED_SEND
import com.example.wise_memory_optimizer.ui.battery.service.BatteryService.ACTION_BATTERY_NEED_UPDATE
import com.example.wise_memory_optimizer.utils.ChargeUtils

class PowerConnectionReceiver : BroadcastReceiver() {
    var animation: Bundle? = null
    var batteryLevelDate: Long = 0
    var context: Context? = null
    var isCharged = false
    var screenOff = false
    var sharedPref: SharedPreferences? = null
    override fun onReceive(context2: Context, intent: Intent) {
        context = context2
        animation = ActivityOptions.makeCustomAnimation(context2, R.anim.fade_in, R.anim.fade_out)
            .toBundle()
        controlAction(intent)
    }

    private fun controlAction(intent: Intent) {
//        if (intent.action == BatteryService.ACTION_BATTERY_CHANGED_SEND) {
        if (intent.action == "android.intent.action.BATTERY_CHANGED") {
            //LogUtil.e("#PowerConnectionReceiver - controlAction - android.intent.action.BATTERY_CHANGED");
            ChargeUtils.BATTERY_LEVEL = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            ChargeUtils.BATTERY_STATUS_FULL = status == BatteryManager.BATTERY_STATUS_FULL

            // How are we charging?
            ChargeUtils.BATTERY_PLUGGED = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        } else if (intent.action == "android.intent.action.BATTERY_LOW") {
            //LogUtil.e("#PowerConnectionReceiver - controlAction - android.intent.action.BATTERY_LOW");
        } else if (intent.action == "android.intent.action.ACTION_POWER_CONNECTED") {
//            Toast.makeText(context, "ACTION_POWER_CONNECTED", Toast.LENGTH_SHORT).show();
//            HawkHelper.setChargerConnected(true);
            ChargeUtils.BATTERY_PLUGGED = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        } else if (intent.action == "android.intent.action.ACTION_POWER_DISCONNECTED") {
//            Toast.makeText(context, "ACTION_POWER_DISCONNECTED", Toast.LENGTH_SHORT).show();
//            HawkHelper.setChargerConnected(false);
            ChargeUtils.BATTERY_PLUGGED = 0
        } else if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            //Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
            val intentService = Intent(context, PowerService::class.java)
            if (Build.VERSION.SDK_INT >= 26) {
                context!!.startForegroundService(intentService)
            } else {
                context!!.startService(intentService)
            }
        }
    }
}